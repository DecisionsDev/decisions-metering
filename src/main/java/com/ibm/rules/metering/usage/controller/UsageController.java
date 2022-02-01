/*
 *
 *   Copyright IBM Corp. 2022
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
package com.ibm.rules.metering.usage.controller;

import java.util.List;
import java.util.Optional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.rules.metering.GenericResponse;
import com.ibm.rules.metering.Response;
import com.ibm.rules.metering.reporting.MetricConfiguration;
import com.ibm.rules.metering.reporting.MetricDefinition;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.rules.metering.usage.Usage;
import com.ibm.rules.metering.usage.UsageRequest;
import com.ibm.rules.metering.usage.repository.UsageEntity;
import com.ibm.rules.metering.usage.repository.UsageService;
import com.ibm.rules.metering.util.Environment;
import com.ibm.rules.metering.util.Messages;

@RestController("usage")
@RequestMapping(value = "/api/usage", produces = "application/json")
public class UsageController {
	private final static Logger LOGGER = Environment.getLogger();
		
	@Autowired
	UsageService usageService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<GenericResponse> submitUsage(@RequestBody List<UsageRequest> usageRequests) {
		if(usageRequests == null) {
			LOGGER.warn(Environment.getMessage(Messages.INVALID_USAGE_REPORT_REQUEST_RECEIVED));
			return new ResponseEntity<GenericResponse>(Response.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
		}
		
		if(usageRequests.size() == 0) {
			LOGGER.warn(Environment.getMessage(Messages.EMPTY_USAGE_REPORT_REQUEST_RECEIVED));
			return new ResponseEntity<GenericResponse>(Response.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
		}
		
		if(LOGGER.isTraceEnabled()) {
			try {
				String request = Environment.serializeObjectToJson(usageRequests);
				LOGGER.trace(Environment.getMessage(Messages.USAGE_REPORT_REQUEST_RECEIVED, request));
			}
			catch (JsonProcessingException e) {
				LOGGER.trace(Environment.getMessage(Messages.USAGE_REPORT_REQUEST_RECEIVED_NO_INFO, e));
			}
		}
		
		try {
			MetricConfiguration configuration = Environment.METRIC_CONFIGURATION;
			
			for (UsageRequest usageRequest : usageRequests) {
				// checking if the usage is to be ignored or not
				boolean recordUsage = true;
				
				if(! configuration.isRecordingOfUnecessaryMetricsEnabled()) {
					recordUsage = false;
					
					// for usage to be recorded,
					// it has to include at least a qualifying metric
					for(Usage usage: usageRequest.getUsageList()) {
						if(configuration.getMetricDefinitions().stream().map(d -> d.getOriginMetricName()).anyMatch(m -> m.equals(usage.getMetricType()))	){
							
							Double value = usage.getMetricValue().doubleValue();
							if(value == 0d) {
								Optional<MetricDefinition> def = configuration.getMetricDefinitions().stream().filter(u -> u.getOriginMetricName().equals(usage.getMetricType())).findFirst();
								
								if(! def.isPresent() || def.get().isRecordingOfNullUsageEnabled()) {
									recordUsage = true;
									break;
								}
							}
							else {
								recordUsage = true;
								break;
							}
						}
					}
				}
				
				if(recordUsage) {
					try {
						usageService.saveOrUpdate(UsageEntity.fromUsageRequest(usageRequest));
						if(LOGGER.isDebugEnabled()) LOGGER.debug(Environment.getMessage(Messages.USAGE_REPORT_REQUEST_SAVED));
					}
					catch(Exception e) {
						LOGGER.error(Environment.getMessage(Messages.USAGE_REPORT_REQUEST_SAVE_ERROR), e);
					}
				}
				else {
					if(LOGGER.isDebugEnabled()) LOGGER.debug(Environment.getMessage(Messages.USAGE_REPORT_REQUEST_IRRELEVANT));
				}
			}
			return new ResponseEntity<GenericResponse>(Response.CREATED_SUCCESSFULLY, HttpStatus.CREATED);
		}
		catch(Exception e) {
			LOGGER.error(Environment.getMessage(Messages.USAGE_REPORT_REQUEST_PROCESSING_ERROR), e);
			return new ResponseEntity<GenericResponse>(Response.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
