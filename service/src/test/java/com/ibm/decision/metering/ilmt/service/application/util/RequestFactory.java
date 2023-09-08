/*
 *
 *   Copyright IBM Corp. 2023
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
package com.ibm.decision.metering.ilmt.service.application.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ibm.decision.metering.ilmt.model.registration.RegistrationRequest;
import com.ibm.decision.metering.ilmt.model.usage.UsageRequest;
import com.ibm.decision.metering.ilmt.service.reporting.MetricConfiguration;
import com.ibm.decision.metering.ilmt.service.util.Environment;

@Component
@EnableConfigurationProperties
public class RequestFactory {
	
	@Value("${com.ibm.decision.metering.ilmt.service.metricsConfiguration}")
	String metricConfigurationValue;
	
	private RequestFactory() {
	}
	
	private final static String REGISTRATION_TEMPLATE = "/registration.json";
	private final static String USAGE_TEMPLATE = "/usage_{0}.json";
	public final static String USAGE_TYPE_DECISIONS = "DECISIONS";
	public final static String USAGE_TYPE_ARTIFACTS = "ALL_ARTIFACTS_BILLABLE";
	
	public MetricConfiguration getMetricConfiguration() throws JsonMappingException, JsonProcessingException, IOException {
		 return MetricConfiguration.read(this.metricConfigurationValue);
	}
	
	public String createRegistrationRequest(String instanceIdentifier, String persistentId, String installDirectory) throws Exception {
		String serializedRegistration = new String(toByteArray(TestUtility.class.getResourceAsStream(REGISTRATION_TEMPLATE)));
		RegistrationRequest registration = Environment.deserializeObjectFromJson(serializedRegistration, RegistrationRequest.class);
		registration.setInstanceIdentifier(instanceIdentifier);
		registration.setInstallDirectory(installDirectory);
		registration.getProducts().get(0).setPersistentId(persistentId);
		return Environment.serializeObjectToJsonOrDefault(registration, null);
	}
	
	public String createUsageRequest(String usageType, long startTime, long endTime, long decisions, String instanceIdentifier, String installDirectory) throws Exception {
		String usageTemplate = MessageFormat.format(USAGE_TEMPLATE, usageType);
		String serializedUsage = new String(toByteArray(TestUtility.class.getResourceAsStream(usageTemplate)));
		UsageRequest usage = Environment.deserializeObjectFromJson(serializedUsage, UsageRequest.class);
		usage.setStartTime(startTime);
		usage.setEndTime(endTime);
		usage.getUsageList().stream().filter(u -> u.getMetricType().equals(usageType)).forEach(u -> u.setMetricValue(decisions));
		usage.setInstanceIdentifier(instanceIdentifier);
		usage.setInstallDirectory(installDirectory);
		
		List<UsageRequest> request = new ArrayList<UsageRequest>();
		request.add(usage);
		return Environment.serializeObjectToJsonOrDefault(request, null);
	}
	
	private static byte[] toByteArray(InputStream stream) throws IOException {
        if (null == stream) {
            return null;
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copy(stream, baos);
        baos.flush();
        baos.close();
        return baos.toByteArray();
    }
	
	private static void copy(InputStream input, OutputStream output) throws IOException {
	    byte[] buffer = new byte[1024];
	    int i = 0;
	    while ((i = input.read(buffer)) != -1) {
	      output.write(buffer, 0, i);
	    }
	}
}
