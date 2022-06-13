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
package com.ibm.rules.metering.application;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.ibm.rules.metering.reporting.MetricConfiguration;
import com.ibm.rules.metering.util.Environment;
import com.ibm.rules.metering.util.Messages;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ibm.rules.metering.application", "com.ibm.rules.metering.registration.repository", "com.ibm.rules.metering.registration", "com.ibm.rules.metering.usage", "com.ibm.rules.metering.reporting", "com.ibm.rules.metering.reporting.repository", "com.ibm.rules.metering.metric.repository"})
@EnableJpaRepositories(basePackages = {"com.ibm.rules.metering.registration.repository", "com.ibm.rules.metering.usage.repository", "com.ibm.rules.metering.reporting.repository", "com.ibm.rules.metering.metric.repository"})
@EntityScan(basePackages = {"com.ibm.rules.metering.registration.repository", "com.ibm.rules.metering.usage.repository", "com.ibm.rules.metering.reporting.repository", "com.ibm.rules.metering.metric.repository"})
@EnableScheduling
public class MeteringServiceApplication {
	private final static String COMMAND_LINE_PROPERTY_PREFIX = "--";
	private final static String COMMAND_LINE_PROPERTY_SEPARATOR = "=";
	private final static String TARGET_PROPERTY_MAPPING_PATTERN = "target_";
	private final static String TARGET_PROPERTY_PREFIX_PATTERN = "prefix_";
	
	private final static Logger LOGGER = Environment.getLogger();
	
	@Value("${com.ibm.rules.metering.metricsConfiguration}")
	String metricConfigurationValue;
	
	@Value("${com.ibm.rules.metering.excludedMetricsCategory}")
	String excludedMetricsCategory;
	
	public static void main(String[] args) {
		LOGGER.info(Environment.getMessage(Messages.APPLICATION_STARTING, Environment.getMessage(Messages.PRODUCT_NAME)));
		
		try {
			// retrieve the properties
			Map<String, Object> applicationProperties = retrieveProperties(args);
			
			// run the application
			SpringApplication app = new SpringApplication(MeteringServiceApplication.class);
			app.setDefaultProperties(applicationProperties);
			app.run(args);
		}
		catch(Exception e) {
			LOGGER.error(Environment.getMessage(Messages.GENERIC_ERROR), e);
		}
	}
	
	private static Map<String, Object> retrieveProperties(String[] args){
		Map<String, Object> applicationProperties = new HashMap<String, Object>();
		
		try {
			Properties mandatoryProperties = Environment.getDefaultProperties();

			Map<String, String> commandLineProperties = Arrays.stream(args)
				.filter(c -> c.startsWith(COMMAND_LINE_PROPERTY_PREFIX))
				.map(c -> c.substring(COMMAND_LINE_PROPERTY_PREFIX.length()))
				.map(c -> c.split(COMMAND_LINE_PROPERTY_SEPARATOR))
				.filter(kv -> kv != null && kv.length > 1)
				.collect(Collectors.toMap(kv -> { return kv[0]; }, kv -> { return kv[1]; }));
			
			commandLineProperties.keySet().stream()
				.filter(k -> ! mandatoryProperties.containsKey(k))
				.forEach(k -> applicationProperties.put(k, commandLineProperties.get(k)));
			
			mandatoryProperties.entrySet().stream()
				.filter(e -> ! e.getKey().toString().startsWith(TARGET_PROPERTY_PREFIX_PATTERN) && ! e.getKey().toString().startsWith(TARGET_PROPERTY_MAPPING_PATTERN))
				.forEach(e -> {
					String targetProperty = mandatoryProperties.getProperty(TARGET_PROPERTY_MAPPING_PATTERN+e.getKey());
					String targetValue = (commandLineProperties.get(e.getKey()) != null) ? commandLineProperties.get(e.getKey()).toString() : null;
					if(targetValue == null) targetValue = System.getProperty(e.getKey().toString());
					if(targetValue == null) targetValue = mandatoryProperties.getProperty(e.getKey().toString());					
					if(mandatoryProperties.get(TARGET_PROPERTY_PREFIX_PATTERN+e.getKey().toString()) != null) targetValue = mandatoryProperties.get(TARGET_PROPERTY_PREFIX_PATTERN+e.getKey().toString()) + targetValue;
					applicationProperties.put(targetProperty, targetValue);
				});
		}
		catch(Exception e) {
			LOGGER.error(Environment.getMessage(Messages.INIT_PROPERTIES_ERROR), e);
		}
		
		return applicationProperties;
	}

	@PostConstruct
    private void init() {
		try {
			Environment.METRIC_CONFIGURATION = MetricConfiguration.read(this.metricConfigurationValue);
				
			if(excludedMetricsCategory != null && ! "".equals(excludedMetricsCategory)) {
				Environment.METRIC_CONFIGURATION.filter(excludedMetricsCategory);
			}
		} catch (Exception e) {
			LOGGER.error(Environment.getMessage(Messages.CONFIGURATION_ERROR), e);
		}
		
		LOGGER.info(Environment.getMessage(Messages.APPLICATION_READY, Environment.getMessage(Messages.PRODUCT_NAME)));
    }
}
