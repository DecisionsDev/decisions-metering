/*
 *
 *   Copyright IBM Corp. 2024
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package com.ibm.decision.metering.ilmt.service.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.ibm.decision.metering.ilmt.service.application.util.RequestFactory;
import com.ibm.decision.metering.ilmt.service.registration.repository.RegistrationService;
import com.ibm.decision.metering.ilmt.service.usage.repository.UsageService;
import com.ibm.decision.metering.ilmt.service.util.Environment;
import com.ibm.decision.metering.ilmt.service.util.Messages;

@RunWith(SpringRunner.class)
@SpringBootTest(properties= { }, webEnvironment = WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EndpointsTest {
	
	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private UsageService usageService;
	
	@Autowired
	private RequestFactory requestFactory;
	
	@LocalServerPort
	private int port;
	
	@Test
    public void testSimpleReporting() throws Exception {
		CloseableHttpClient client = null;
		try{
			client = HttpClients.createDefault();
			HttpGet request = new HttpGet(getBaseURL()+"/");
			CloseableHttpResponse response = client.execute(request);
			String responseText = EntityUtils.toString(response.getEntity());
			assertThat(responseText).startsWith(Environment.getMessage(Messages.PRODUCT_NAME));
		}
		finally {
			if(client != null) client.close();
		}
	}
	
	@Test
    public void testRegistration() throws Exception {
		String request = requestFactory.createRegistrationRequest("sample_endpoint", UUID.randomUUID().toString(), "/my/server/sample/endpoint");

		Response response = org.apache.http.client.fluent.Request
			.Post(getRegistrationURL())
			.bodyString(request, ContentType.APPLICATION_JSON)
			.execute();

		HttpResponse httpResponse = response.returnResponse();
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		String responseContent = EntityUtils.toString(httpResponse.getEntity());

		assertThat(statusCode).isEqualTo(201);
		assertThat(responseContent).contains("Created Successfully");
	}
	
	@Test
    public void testRegistrationData() throws Exception {
		int registrationsBefore = registrationService.getAllRegistrations().size();
		assertThat(registrationsBefore).isGreaterThanOrEqualTo(0);

		String request = requestFactory.createRegistrationRequest("dummy_endpoint", UUID.randomUUID().toString(), "/my/server/dummy/endpoint");
		org.apache.http.client.fluent.Request
			.Post(getRegistrationURL())
			.bodyString(request, ContentType.APPLICATION_JSON)
			.execute();

		int registrationsAfter = registrationService.getAllRegistrations().size();
		assertThat(registrationsAfter).isEqualTo(registrationsBefore+1);

		request = requestFactory.createRegistrationRequest("dummy_endpoint_2", UUID.randomUUID().toString(), "/my/server/dummy/endpoint_2");
		org.apache.http.client.fluent.Request
			.Post(getRegistrationURL())
			.bodyString(request, ContentType.APPLICATION_JSON)
			.execute();

		registrationsAfter = registrationService.getAllRegistrations().size();
		assertThat(registrationsAfter).isEqualTo(registrationsBefore+2);
	}
	
	@Test
    public void testUsage() throws Exception {
		long dateTime = Instant.now().toEpochMilli();
		String request = requestFactory.createUsageRequest(RequestFactory.USAGE_TYPE_DECISIONS, dateTime, dateTime, 10, "test_endpoint", "/my/server/test/endpoint");
		Response response = org.apache.http.client.fluent.Request
			.Post(getUsageURL())
			.bodyString(request, ContentType.APPLICATION_JSON)
			.execute();

		HttpResponse httpResponse = response.returnResponse();
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		String responseContent = EntityUtils.toString(httpResponse.getEntity());

		assertThat(statusCode).isEqualTo(201);
		assertThat(responseContent).contains("Created Successfully");
	}
	
	@Test
    public void testUsageData() throws Exception {
		int usagesBefore = usageService.getAllUsages().size();
		assertThat(usagesBefore).isGreaterThanOrEqualTo(0);
		
		long dateTime = Instant.now().toEpochMilli();
		String request = requestFactory.createUsageRequest(RequestFactory.USAGE_TYPE_DECISIONS, dateTime, dateTime, 115, "test_endpoint_2", "/my/server/test/endpoint_2");
		org.apache.http.client.fluent.Request
			.Post(getUsageURL())
			.bodyString(request, ContentType.APPLICATION_JSON)
			.execute();
		
		int usagesAfter = usageService.getAllUsages().size();
		assertThat(usagesAfter).isEqualTo(usagesBefore+1);
		
		dateTime = Instant.now().toEpochMilli();
		request = requestFactory.createUsageRequest(RequestFactory.USAGE_TYPE_DECISIONS, dateTime, dateTime, 220, "test_endpoint_3", "/my/server/test/endpoint_3");
		org.apache.http.client.fluent.Request
			.Post(getUsageURL())
			.bodyString(request, ContentType.APPLICATION_JSON)
			.execute();
		
		usagesAfter = usageService.getAllUsages().size();
		assertThat(usagesAfter).isEqualTo(usagesBefore+2);
	}
	
	private String getRegistrationURL() {
		return getBaseURL()+"/api/startup";
	}
	
	private String getUsageURL() {
		return getBaseURL()+"/api/usage";
	}
	
	private String getBaseURL() {
		return "http://localhost:"+port;
	}
}
