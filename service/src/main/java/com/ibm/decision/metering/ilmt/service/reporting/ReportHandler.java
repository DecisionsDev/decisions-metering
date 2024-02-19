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
package com.ibm.decision.metering.ilmt.service.reporting;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ibm.decision.metering.ilmt.model.registration.RegistrationRequest;
import com.ibm.decision.metering.ilmt.model.usage.Usage;
import com.ibm.decision.metering.ilmt.model.usage.UsageRequest;
import com.ibm.decision.metering.ilmt.service.metric.repository.MetricEntity;
import com.ibm.decision.metering.ilmt.service.metric.repository.MetricRepository;
import com.ibm.decision.metering.ilmt.service.metric.repository.RemainderStatus;
import com.ibm.decision.metering.ilmt.service.registration.repository.RegistrationEntity;
import com.ibm.decision.metering.ilmt.service.registration.repository.RegistrationRepository;
import com.ibm.decision.metering.ilmt.service.registration.repository.RegistrationService;
import com.ibm.decision.metering.ilmt.service.reporting.repository.ReportEntity;
import com.ibm.decision.metering.ilmt.service.reporting.repository.ReportRepository;
import com.ibm.decision.metering.ilmt.service.reporting.repository.ReportStatus;
import com.ibm.decision.metering.ilmt.service.usage.repository.UsageEntity;
import com.ibm.decision.metering.ilmt.service.usage.repository.UsageService;
import com.ibm.decision.metering.ilmt.service.util.Environment;
import com.ibm.decision.metering.ilmt.service.util.Messages;

@Component
public class ReportHandler {
	private final static Logger LOGGER = Environment.getLogger();
	
	@Autowired
    UsageService usageService;
	
	@Autowired
    RegistrationRepository registrationRepository;
	
	@Autowired
	RegistrationService registrationService;
	
	@Autowired
	ReportRepository reportRepository;
	
	@Autowired
	MetricRepository metricRepository;
	
	@Value("${com.ibm.decision.metering.ilmt.service.metricsConfiguration}")
	String metricConfigurationValue;
	
	@Value("${com.ibm.decision.metering.ilmt.service.ILMTsoftwareName}")
	private String softwareName;
	
	@Value("${com.ibm.decision.metering.ilmt.service.ILMToutputDirectory}")
	private String outputDirectory;
	
	MetricConfiguration metricConfiguration = null;
	
	@Scheduled(fixedRateString="${com.ibm.decision.metering.ilmt.service.processingRate}", initialDelayString = "${com.ibm.decision.metering.ilmt.service.processingInitialDelay}")
	public synchronized void doReporting() {
		if(LOGGER.isDebugEnabled()) LOGGER.debug(Environment.getMessage(Messages.REPORTING_START));
		
		String sessionId = null;
		try {			
			MetricConfiguration configuration = Environment.METRIC_CONFIGURATION;
			if(configuration == null || configuration.getMetricDefinitions().isEmpty()) {
				LOGGER.error(Environment.getMessage(Messages.IMPOSSIBLE_PROCESSING_INVALID_CONFIGURATION));
				return;
			}
			
			// retrieve usages not yet processed
			List<UsageEntity> usages = usageService.getAllUsagesNotReported();
			if(usages == null || usages.isEmpty()) {
				if(LOGGER.isDebugEnabled()) LOGGER.debug(Environment.getMessage(Messages.NO_USAGE_TO_PROCESS));
				return;
			}
			
			if(LOGGER.isTraceEnabled()) LOGGER.trace(Environment.getMessage(Messages.USAGES_TO_PROCESS, usages.size()));
			
			// preparing computation
			List<MetricDefinition> metricsToCompute = configuration.getMetricDefinitions();
			MetricAggregator aggregator = new MetricAggregator();
			// retrieve useful registration information
			Map<String, RegistrationRequest> registrations = 
					registrationService.getAllRegistrations().stream().collect(Collectors.toMap(RegistrationEntity::getId, r -> (r.toRegistrationRequest()))); //.getProducts().size() > 0) ? r.toRegistrationRequest().getProducts().get(0).getPersistentId() : null));		
			
			if(registrations.isEmpty()) {
				LOGGER.error(Environment.getMessage(Messages.IMPOSSIBLE_PROCESSING_NO_REGISTRATION));
				return;
			}
			
			// creating a new report entity
			ReportEntity report = new ReportEntity();
			reportRepository.save(report);		
			sessionId = report.getId();
			if(LOGGER.isTraceEnabled()) LOGGER.trace(Environment.getMessage(Messages.USAGES_PROCESSING_SESSION_ID, sessionId));
			
			List<UsageEntity> processed = new ArrayList<UsageEntity>();
			for(UsageEntity usage : usages) {
				UsageRequest request = usage.toUsageRequest();
				
				List<Usage> actualUsages = request.getUsageList();
				for(Usage actualUsage : actualUsages) {
					Optional<MetricDefinition> metricDefinition = metricsToCompute.stream().filter(d -> d.getOriginMetricName().equals(actualUsage.getMetricType())).findFirst();
					
					if(metricDefinition.isPresent()) {
						String softwarePath = request.getInstallDirectory();
						RegistrationRequest registration = registrations.get(request.getInstanceIdentifier());
						String persistentId = (registration != null && registration.getProducts().size() > 0) ? registration.getProducts().get(0).getPersistentId() : null;
						
						if(persistentId == null) {
							LOGGER.warn(Environment.getMessage(Messages.UNKNOWN_SOFTWARE_INSTANCE, usage.getId(), usage.getDefinition()));
							continue;
						}
						
						if(softwarePath == null || softwarePath.trim().equals("") || ! softwarePath.startsWith("/")) {
							softwarePath = "/";
							if(LOGGER.isDebugEnabled()) LOGGER.debug(Environment.getMessage(Messages.INVALID_SOFTWARE_PATH, usage.getId(), usage.getDefinition()));
						}
						
						MetricIdentification metricIdentification = new MetricIdentification(metricDefinition.get(), softwarePath, persistentId);
						aggregator.aggregateValue(metricIdentification, actualUsage, request.getEndTime());
						usage.setReport(report.getId());
						usageService.saveOrUpdate(usage);
						processed.add(usage);
					}
				}
			}
			
			if(LOGGER.isDebugEnabled()) LOGGER.debug(Environment.getMessage(Messages.USAGES_USED_FOR_PROCESSING, processed.size())); 
			
			report.setStatus(ReportStatus.IN_PROCESS);
			reportRepository.save(report);
			
			if(aggregator.getMetricAggregations().isEmpty()) {
				report.setStatus(ReportStatus.NOT_SUBMITTED);
				reportRepository.save(report);
				if(LOGGER.isDebugEnabled()) LOGGER.debug(Environment.getMessage(Messages.NO_AGGREGATION_TO_DO));
			}
			else {
				for(MetricIdentification metric : aggregator.getMetricAggregations().keySet()) {
					MetricDefinition definition = metric.getMetricDefinition();
					
					String completeInstanceIdentifier = null;
						completeInstanceIdentifier = registrations.entrySet().stream()
							.filter(e -> e.getValue().getProducts() != null 
									&& e.getValue().getProducts().size() > 0
									&& metric.getPersistentId().equals(e.getValue().getProducts().get(0).getPersistentId())
									&& metric.getSoftwarePath().equals(e.getValue().getInstallDirectory()))
							.map(e -> e.getKey())
							.findFirst()
							.orElse(null);
					if(completeInstanceIdentifier == null) completeInstanceIdentifier = metric.getPersistentId() + "@" + metric.getSoftwarePath();
					
					// computed value to report
					Double metricValue = aggregator.getMetricAggregations().get(metric).getValue();
					String metricName = definition.getFormatter().getMetricName(metricValue);
					if(LOGGER.isDebugEnabled()) LOGGER.debug(Environment.getMessage(Messages.TOTAL_USAGES_RESULT, definition.getDestinationMetricName(), completeInstanceIdentifier, metricValue));
					
					MetricEntity metricStatus = null; 
					if(definition.isUsingRemainders()) {
						metricStatus = metricRepository.findByInstanceIdAndMetric(metric.getPersistentId(), metric.getSoftwarePath(), metricName).stream().findFirst().orElse(null);
						if(metricStatus != null && metricStatus.getRemainderStatus() == RemainderStatus.TO_BE_PROCESSED) {
							metricValue = aggregator.aggregateRemainder(metric, metricStatus.getRemainder());
							metricStatus.setRemainderStatus(RemainderStatus.IN_PROCESS);
							metricRepository.save(metricStatus);

							if(LOGGER.isDebugEnabled()) LOGGER.debug(Environment.getMessage(Messages.REMAINDER_FOUND, metricStatus.getRemainder(), definition.getDestinationMetricName(), completeInstanceIdentifier, metricValue));
						}
						else {
							if(LOGGER.isTraceEnabled()) LOGGER.trace(Environment.getMessage(Messages.NO_REMAINDER_FOUND, definition.getDestinationMetricName(), completeInstanceIdentifier));
						}
					}
										
					long oldestUsageTimestamp = aggregator.getMetricAggregations().get(metric).getOldestDate();
					long newestUsageTimestamp = aggregator.getMetricAggregations().get(metric).getNewestDate();
					DecisionMeteringReport meteringReport = new DecisionMeteringReport(this.softwareName, metric.getPersistentId(), metric.getSoftwarePath(), definition.getFormatter(), metricValue, oldestUsageTimestamp, newestUsageTimestamp, this.outputDirectory);
					report.setDefinition(Environment.serializeObjectToJsonOrDefault(meteringReport, null));
					report.setCompletionDate(Instant.now().toEpochMilli());
					
					if(metricValue == 0d && ! definition.isReportingNullMetricValueEnabled()) {
						if(LOGGER.isDebugEnabled()) LOGGER.debug(Environment.getMessage(Messages.NULL_METRIC_NOT_REPORTED, metricName, completeInstanceIdentifier));
						report.setStatus(ReportStatus.NOT_SUBMITTED);
					}
					else {
						if(LOGGER.isDebugEnabled()) LOGGER.debug(Environment.getMessage(Messages.REPORTING_METRIC, metricValue, metricName, completeInstanceIdentifier));
						
						meteringReport.writeILMTFile();
						report.setStatus(ReportStatus.SUBMITTED);
						LOGGER.info(Environment.getMessage(Messages.ILMT_WRITE_SUCCESS, this.outputDirectory, metricName, completeInstanceIdentifier));
						if(LOGGER.isDebugEnabled()) LOGGER.debug(Environment.getMessage(Messages.ILMT_WRITE_DETAILS, meteringReport.getReportedMetricValue(), metricName, completeInstanceIdentifier, this.outputDirectory));
					}
					
					// compute potential unreported usages (due to rounding)
					if(definition.isUsingRemainders()) {
						double remainder = definition.getFormatter().getMetricRemainder(metricValue);
						if(metricStatus == null) {
							metricStatus = new MetricEntity();
							metricStatus.setPersistentId(metric.getPersistentId());
							metricStatus.setSoftwarePath(metric.getSoftwarePath());
							metricStatus.setMetricName(metricName);
						}
						if(remainder > 0 ) {
							metricStatus.setRemainder(remainder);
							metricStatus.setRemainderStatus(RemainderStatus.TO_BE_PROCESSED);
							LOGGER.info(Environment.getMessage(Messages.REMAINDER_TO_BE_PROCESSED, remainder, metricName, completeInstanceIdentifier));
						}
						else {
							metricStatus.setRemainder(0);
							metricStatus.setRemainderStatus(RemainderStatus.NO_PROCESSING_NEEDED);
							if(LOGGER.isDebugEnabled()) LOGGER.debug(Environment.getMessage(Messages.NO_REMAINDER_TO_BE_PROCESSED, metricName, completeInstanceIdentifier));
						}
						metricStatus.setLastUpdateDate(Instant.now().toEpochMilli());
						metricRepository.save(metricStatus);
					}
					
					// update report
					reportRepository.save(report);
				}
			}
			// removing usages that were processed
			int usagesToDelete = processed.size();
			final AtomicInteger deletedUsagesCount = new AtomicInteger(0);
			try {
				processed.stream().forEach(p -> { usageService.delete(p.getId()); deletedUsagesCount.getAndIncrement(); });
				if(LOGGER.isDebugEnabled()) LOGGER.debug(Environment.getMessage(Messages.PROCESSING_DELETE_USAGES_SUCCESS, deletedUsagesCount.longValue()));
			}
			catch(Exception e) {
				LOGGER.error(Environment.getMessage(Messages.PROCESSING_DELETE_USAGES_ERROR, usagesToDelete, deletedUsagesCount.longValue()), e);
			}
			
			/************/
			/* cleanup */
			/************/
			try {
				if(LOGGER.isTraceEnabled()) LOGGER.trace(Environment.getMessage(Messages.CLEANUP_START));
				
				// if there are remainders IN PROCESS, they come from a processing that 
				// did not go until the end => flagging them back as TO BE PROCESSED
				List<MetricEntity> metricsWithRemaindersInProcess = metricRepository.findByRemainderStatus(RemainderStatus.IN_PROCESS);
				if(metricsWithRemaindersInProcess != null && ! metricsWithRemaindersInProcess.isEmpty()) {
					metricsWithRemaindersInProcess.forEach(m -> {
						m.setRemainderStatus(RemainderStatus.TO_BE_PROCESSED);
						m.setLastUpdateDate(Instant.now().toEpochMilli());
						metricRepository.save(m);
						LOGGER.info(Environment.getMessage(Messages.CLEANUP_REMAINDER_TO_PROCESS, m.getRemainder(), m.getMetricName(), m.getPersistentId()+"@"+m.getSoftwarePath()));
					});
				}
				else {
					if(LOGGER.isTraceEnabled()) LOGGER.trace(Environment.getMessage(Messages.CLEANUP_NO_REMAINDER_TO_PROCESS));
				}
				
				// examine usages that are linked to a report
				List<UsageEntity> usagesWithReportId = usageService.getAllUsagesWithReportId();
				Map<String, ReportEntity> reports = new HashMap<String, ReportEntity>();
				long deletedUsages = 0;
				long unflaggedUsages = 0;
				if(! usagesWithReportId.isEmpty()) {
					for(UsageEntity usage: usagesWithReportId) {
						String reportId = usage.getReport();
						ReportEntity correspondingReport = reports.get(reportId);
						if(correspondingReport == null) {
							Optional<ReportEntity> retrievedReport = reportRepository.findById(reportId);
							if(retrievedReport.isPresent()) correspondingReport = retrievedReport.get();
						}
						
						// remove the usages corresponding to finalized reports
						if(correspondingReport != null && ReportStatus.isFinal(correspondingReport.getStatus())) {
							usageService.delete(usage.getId());
							deletedUsages++;
						}
						// remove report link from usages when the corresponding report was not finalized or does not exist
						else {
							usage.setReport(null);
							usageService.saveOrUpdate(usage);
							unflaggedUsages++;
						}
					}
				}
				
				if(deletedUsages > 0) {
					LOGGER.info(Environment.getMessage(Messages.CLEANUP_DELETED_RECORDS, deletedUsages));
				}
				else {
					if(LOGGER.isTraceEnabled()) LOGGER.trace(Environment.getMessage(Messages.CLEANUP_NO_DELETED_RECORDS));
				}
				
				if(unflaggedUsages > 0) {
					LOGGER.info(Environment.getMessage(Messages.CLEANUP_UNFLAGGED_RECORDS, unflaggedUsages));
				}
				else {
					if(LOGGER.isTraceEnabled()) LOGGER.trace(Environment.getMessage(Messages.CLEANUP_NO_UNFLAGGED_RECORDS));
				}
				
				if(LOGGER.isTraceEnabled()) LOGGER.trace(Environment.getMessage(Messages.CLEANUP_END));
			}
			catch(Exception e) {
				LOGGER.error(Environment.getMessage(Messages.CLEANUP_ERROR), e);
			}
			/************/
		}
		catch(Exception e) {
			LOGGER.error(Environment.getMessage(Messages.REPORT_PROCESSING_ERROR), e);
		}
		finally {
			if(LOGGER.isDebugEnabled()) {
				if(sessionId != null) {
					LOGGER.debug(Environment.getMessage(Messages.REPORTING_END_WITH_ID, sessionId));
				}
				else {
					LOGGER.debug(Environment.getMessage(Messages.REPORTING_END));
				}
			}
		}

		
  }
}
