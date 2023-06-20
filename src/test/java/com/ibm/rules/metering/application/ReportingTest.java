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
package com.ibm.rules.metering.application;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.ibm.rules.metering.application.model.LicenseFile;
import com.ibm.rules.metering.application.model.Metric;
import com.ibm.rules.metering.application.model.Usage;
import com.ibm.rules.metering.application.util.MetricExpectation;
import com.ibm.rules.metering.application.util.RemainderExpectation;
import com.ibm.rules.metering.application.util.RequestFactory;
import com.ibm.rules.metering.application.util.TestUtility;
import com.ibm.rules.metering.metric.repository.MetricEntity;
import com.ibm.rules.metering.metric.repository.MetricRepository;
import com.ibm.rules.metering.metric.repository.RemainderStatus;

@RunWith(SpringRunner.class)
@SpringBootTest(properties= { }, webEnvironment = WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReportingTest {
	
	@Autowired
    private TestUtility testUtility;
	
	@Value("${com.ibm.rules.metering.ILMToutputDirectory}")
	private String licenseFilesOutputDirectory;
	
	@Value("${com.ibm.rules.metering.ILMTsoftwareName}")
	private String softwareName;
	
	@Autowired
	MetricRepository metricRepository;
	
	@Test
	public void testDecisionsReportingSingleUsageVeryLowValue() throws Exception {
		String id = "0";
		String persistentId = "a5f38dc5056540d595857e3dd6e14ec4";
		String usageType = RequestFactory.USAGE_TYPE_DECISIONS;
		long usageToReport = 100l;
		long expectedReportedUsages = 0l;
		String metricName = "MILLION_MONTHLY_DECISIONS";
		int unit = 1000000;
		
		List<MetricExpectation> expectedMetrics = Collections.singletonList(new MetricExpectation(metricName, expectedReportedUsages, unit));
		
		testUsageReport(true, id, persistentId, Collections.singletonList(new Usage(usageToReport, usageType)), expectedMetrics);
	}
	
	@Test
	public void testDecisionsReportingSingleUsageLowValue() throws Exception {
		String id = "1";
		String persistentId = "a5f38dc5056540d595857e3dd6e14ec4";
		String usageType = RequestFactory.USAGE_TYPE_DECISIONS;
		long usageToReport = 1400l;
		long expectedReportedUsages = 1000;
		String metricName = "MILLION_MONTHLY_DECISIONS";
		int unit = 1000000;
		
		List<MetricExpectation> expectedMetrics = Collections.singletonList(new MetricExpectation(metricName, expectedReportedUsages, unit));
		
		testUsageReport(true, id, persistentId, Collections.singletonList(new Usage(usageToReport, usageType)), expectedMetrics);
	}
	
	@Test
	public void testDecisionsReportingSingleUsageHighValue() throws Exception {
		String id = "2";
		String persistentId = "a5f38dc5056540d595857e3dd6e14ec4";
		String usageType = RequestFactory.USAGE_TYPE_DECISIONS;
		long usageToReport = 2450000l;
		long expectedReportedUsages = 2450000;
		String metricName = "MILLION_MONTHLY_DECISIONS";
		int unit = 1000000;
		
		List<MetricExpectation> expectedMetrics = Collections.singletonList(new MetricExpectation(metricName, expectedReportedUsages, unit));
		
		testUsageReport(true, id, persistentId, Collections.singletonList(new Usage(usageToReport, usageType)), expectedMetrics);
	}
	
	@Test
	public void testDecisionsReportingMultipleValues() throws Exception {
		String id = "3";
		String persistentId = "a5f38dc5056540d595857e3dd6e14ec4";
		String usageType = RequestFactory.USAGE_TYPE_DECISIONS;
		
		List<Usage> usages = Arrays.asList( new Usage[] {
				new Usage(512496, usageType),
				new Usage(821345, usageType),
				new Usage(41005, usageType),
				new Usage(8, usageType)
		});
		long expectedReportedUsages = 1374000;
		String metricName = "MILLION_MONTHLY_DECISIONS";
		int unit = 1000000;
		
		List<MetricExpectation> expectedMetrics = Collections.singletonList(new MetricExpectation(metricName, expectedReportedUsages, unit));
		
		testUsageReport(true, id, persistentId, usages, expectedMetrics);
	}
	
	@Test
	public void testArtifactsReportingSingleUsageVeryLowValue() throws Exception {
		String id = "4";
		String persistentId = "a5f38dc5056540d595857e3dd6e14ec4";
		String usageType = RequestFactory.USAGE_TYPE_ARTIFACTS;
		long usageToReport = 4l;
		long expectedReportedUsages = 4;
		String metricName = "THOUSAND_MONTHLY_ARTIFACTS";
		int unit = 1000;
		
		List<MetricExpectation> expectedMetrics = Collections.singletonList(new MetricExpectation(metricName, expectedReportedUsages, unit));
		
		testUsageReport(true, id, persistentId, Collections.singletonList(new Usage(usageToReport, usageType)), expectedMetrics);
	}	
	
	@Test
	public void testArtifactsReportingSingleUsageLowValue() throws Exception {
		String id = "5";
		String persistentId = "a5f38dc5056540d595857e3dd6e14ec4";
		String usageType = RequestFactory.USAGE_TYPE_ARTIFACTS;
		long usageToReport = 1980l;
		long expectedReportedUsages = 1980;
		String metricName = "THOUSAND_MONTHLY_ARTIFACTS";
		int unit = 1000;
		
		List<MetricExpectation> expectedMetrics = Collections.singletonList(new MetricExpectation(metricName, expectedReportedUsages, unit));
		
		testUsageReport(true, id, persistentId, Collections.singletonList(new Usage(usageToReport, usageType)), expectedMetrics);
	}
	
	@Test
	public void testArtifactsReportingSingleUsageHighValue() throws Exception {
		String id = "6";
		String persistentId = "a5f38dc5056540d595857e3dd6e14ec4";
		String usageType = RequestFactory.USAGE_TYPE_ARTIFACTS;
		long usageToReport = 2499597l;
		long expectedReportedUsages = 2499597;
		String metricName = "THOUSAND_MONTHLY_ARTIFACTS";
		int unit = 1000;
		
		List<MetricExpectation> expectedMetrics = Collections.singletonList(new MetricExpectation(metricName, expectedReportedUsages, unit));
		
		testUsageReport(true, id, persistentId, Collections.singletonList(new Usage(usageToReport, usageType)), expectedMetrics);
	}
	
	@Test
	public void testArtifactsReportingMultipleValues() throws Exception {
		String id = "7";
		String persistentId = "a5f38dc5056540d595857e3dd6e14ec4";
		String usageType = RequestFactory.USAGE_TYPE_ARTIFACTS;
		
		List<Usage> usages = Arrays.asList( new Usage[] {
				new Usage(1980, usageType),
				new Usage(9215, usageType),
				new Usage(712, usageType)
		});
		long expectedReportedUsages = 9215;
		
		String metricName = "THOUSAND_MONTHLY_ARTIFACTS";
		int unit = 1000;
		
		List<MetricExpectation> expectedMetrics = Collections.singletonList(new MetricExpectation(metricName, expectedReportedUsages, unit));
		
		testUsageReport(true, id, persistentId, usages, expectedMetrics);
	}
	
	@Test
	public void testReportingMultipleUsagesMultipleMixedValues() throws Exception {
		String id = "8";
		String persistentId = "a5f38dc5056540d595857e3dd6e14ec4";
		List<Usage> usages = Arrays.asList( new Usage[] {
				new Usage(2415, RequestFactory.USAGE_TYPE_ARTIFACTS),
				new Usage(850000, RequestFactory.USAGE_TYPE_DECISIONS),
				new Usage(12435, RequestFactory.USAGE_TYPE_ARTIFACTS),
				new Usage(221694, RequestFactory.USAGE_TYPE_DECISIONS),
				new Usage(7654, RequestFactory.USAGE_TYPE_ARTIFACTS),
				new Usage(123455, RequestFactory.USAGE_TYPE_DECISIONS)
		});
		
		List<MetricExpectation> expectedMetrics =  Arrays.asList(new MetricExpectation[] {
				new MetricExpectation("MILLION_MONTHLY_DECISIONS", 1195000, 1000000),
				new MetricExpectation("THOUSAND_MONTHLY_ARTIFACTS", 12435, 1000)
		});
		testUsageReport(true, id, persistentId, usages, expectedMetrics);
	}
	
	@Test
	public void testReportingMultipleUsagesWithRemainders() throws Exception {
		String id = "9";
		String persistentId = "a5f38dc5056540d595857e3dd6e14ec4";
		
		List<Usage> usages = Arrays.asList( new Usage[] {
				new Usage(100, RequestFactory.USAGE_TYPE_DECISIONS),
				new Usage(234, RequestFactory.USAGE_TYPE_DECISIONS),
				new Usage(45, RequestFactory.USAGE_TYPE_DECISIONS)
		}); // total 379
		
		List<MetricExpectation> expectedMetrics =  Arrays.asList(new MetricExpectation[] {
				new MetricExpectation("MILLION_MONTHLY_DECISIONS", 0, 1000000)
		});
		
		long expectedRemainder = usages.stream().mapToLong(u -> u.getReportedValue()).sum();
		List<RemainderExpectation> expectedRemainders = Arrays.asList(new RemainderExpectation[] {
				new RemainderExpectation("MILLION_MONTHLY_DECISIONS", 
						expectedRemainder,
						RemainderStatus.TO_BE_PROCESSED)
		});
		
		testUsageReport(true, id, persistentId, usages, expectedMetrics, expectedRemainders);
		
		usages = Arrays.asList( new Usage[] {
				new Usage(200, RequestFactory.USAGE_TYPE_DECISIONS),
				new Usage(280, RequestFactory.USAGE_TYPE_DECISIONS),	
		}); // total 480
		
		// remainder should be 379 + 480 = 859
		expectedRemainder += usages.stream().mapToLong(u -> u.getReportedValue()).sum();
		expectedRemainders = Arrays.asList(new RemainderExpectation[] {
				new RemainderExpectation("MILLION_MONTHLY_DECISIONS", 
						expectedRemainder,
						RemainderStatus.TO_BE_PROCESSED)
		});
		
		testUsageReport(false, id, persistentId, usages, expectedMetrics, expectedRemainders);
	
		usages = Arrays.asList( new Usage[] {
				new Usage(130, RequestFactory.USAGE_TYPE_DECISIONS),
				new Usage(215, RequestFactory.USAGE_TYPE_DECISIONS),	
		});
		
		expectedMetrics =  Arrays.asList(new MetricExpectation[] {
				new MetricExpectation("MILLION_MONTHLY_DECISIONS", 1000, 1000000)
		});
		
		// usages sum up to 130+215=345
		// and there is an ongoing remainder of 859
		// so it makes 345+859=1204
		// => it should report 1000 and keep a remainder of 204
		expectedRemainder += usages.stream().mapToLong(u -> u.getReportedValue()).sum();
		assertThat(expectedRemainder).isGreaterThan(1000); // 1204
		expectedRemainder = expectedRemainder - 1000; // 204
		expectedRemainders = Arrays.asList(new RemainderExpectation[] {
				new RemainderExpectation("MILLION_MONTHLY_DECISIONS", 
						expectedRemainder,
						RemainderStatus.TO_BE_PROCESSED)
		});
		
		testUsageReport(false, id, persistentId, usages, expectedMetrics, expectedRemainders);
	
		// create usage to complete a reporting with 0 remainder
		usages = Arrays.asList( new Usage[] {
				new Usage(1000 - expectedRemainder, RequestFactory.USAGE_TYPE_DECISIONS)
		});
		expectedMetrics =  Arrays.asList(new MetricExpectation[] {
				new MetricExpectation("MILLION_MONTHLY_DECISIONS", 1000, 1000000)
		});
		expectedRemainders = Arrays.asList(new RemainderExpectation[] {
				new RemainderExpectation("MILLION_MONTHLY_DECISIONS", 
						0,
						RemainderStatus.NO_PROCESSING_NEEDED)
		});

		testUsageReport(false, id, persistentId, usages, expectedMetrics, expectedRemainders);
		
		usages = Arrays.asList( new Usage[] {
				new Usage(999, RequestFactory.USAGE_TYPE_DECISIONS)
		});
		expectedMetrics =  Arrays.asList(new MetricExpectation[] {
				new MetricExpectation("MILLION_MONTHLY_DECISIONS", 0, 1000000)
		});
		expectedRemainders = Arrays.asList(new RemainderExpectation[] {
				new RemainderExpectation("MILLION_MONTHLY_DECISIONS", 
						999,
						RemainderStatus.TO_BE_PROCESSED)
		});

		//testUsageReport(false, id, persistentId, usages, expectedMetrics, expectedRemainders);
	}
	
	private void testUsageReport(boolean register, String id, String persistentId, List<Usage> usagesToReport,
			List<MetricExpectation> expectedMetrics) throws Exception {
		testUsageReport(register, id, persistentId, usagesToReport, expectedMetrics, null);
	}
	
	private void testUsageReport(boolean register, String id, String persistentId, List<Usage> usagesToReport,
			List<MetricExpectation> expectedMetrics, List<RemainderExpectation> expectedRemainders) throws Exception {
		
		testUtility.deleteLicenseFilesDirectoryIfExists();
		assertThat(testUtility.getLicenseFilesCount()).isEqualTo(0);
		
		String instanceId = "test_endpoint_for_reporting_"+id;
		String installationDirectory = "/my/server/test/endpoint_for_reporting_"+id;
		
		// do registration
		if(register) {
			testUtility.sendRegistrationRequest(instanceId, persistentId, installationDirectory);
		}
		
		// send the usage
		long dateTime = Instant.now().toEpochMilli();
		for(Usage usage: usagesToReport) {
			testUtility.sendUsageRequest(usage.getMetricType(), dateTime, dateTime, usage.getReportedValue(), instanceId, installationDirectory);
		}
		
		// wait for processing to happen
		Thread.sleep(15000);
		
		// check that a new license tag file has been written to disk
		assertThat(testUtility.doesOutputLicenseFilesDirectoryExist()).isTrue();
		List<LicenseFile> licenseFiles = testUtility.getLicenseFiles();
		assertThat(licenseFiles.size() == 1);		
		
		// checking the license file
		LicenseFile licenseFile = licenseFiles.get(0);
		assertThat(licenseFile.getSoftwareIdentity()).isNotNull();
		assertThat(licenseFile.getSoftwareIdentity().getInstanceId()).isEqualTo(installationDirectory);
		assertThat(licenseFile.getSoftwareIdentity().getPersistentId()).isEqualTo(persistentId);
		assertThat(licenseFile.getSoftwareIdentity().getName()).isEqualTo(softwareName);
		List<Metric> metrics = licenseFile.getMetrics();
		assertThat(metrics).hasSize(expectedMetrics.size());
		
		for(MetricExpectation expectedMetric : expectedMetrics) {
			Metric metric = metrics.stream().filter(m -> m.getType().equals(expectedMetric.getMetricName())).findFirst().orElse(null);
			assertThat(metric).isNotNull().withFailMessage("Expected metric '"+expectedMetric.getMetricName()+"' was not found in the result");
			assertThat(Double.parseDouble(metric.getValue()) * expectedMetric.getUnit()).isEqualTo(expectedMetric.getExpectedValue());
		}
		
		if(expectedRemainders == null) return;
		
		//String identifier = persistentId+"@"+"/my/server/test/endpoint_for_reporting_"+id;
		for(RemainderExpectation expectedRemainder : expectedRemainders) {
			List<MetricEntity> metricsStatus = metricRepository.findByInstanceIdAndMetric(persistentId, installationDirectory, expectedRemainder.getMetricName());
			assertThat(metricsStatus).isNotNull().hasSize(1);
			MetricEntity metricStatus = metricsStatus.get(0);
			assertThat(metricStatus.getRemainder()).isEqualTo(expectedRemainder.getExpectedValue());
			assertThat(metricStatus.getRemainderStatus()).isEqualTo(expectedRemainder.getExpectedStatus());
		}
	}
}
