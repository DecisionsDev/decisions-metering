/*
 *
 *   Copyright IBM Corp. 2020
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
package com.ibm.rules.metering.reporting;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ibm.rules.metering.util.Environment;

public class MetricConfiguration {
	List<MetricDefinition> metricDefinitions;
	boolean recordingOfUnecessaryMetricsEnabled;

	public List<MetricDefinition> getMetricDefinitions() {
		return metricDefinitions;
	}

	public void setMetricDefinitions(List<MetricDefinition> metricDefinitions) {
		this.metricDefinitions = metricDefinitions;
	}
	
	public boolean isRecordingOfUnecessaryMetricsEnabled() {
		return recordingOfUnecessaryMetricsEnabled;
	}

	public void setRecordingOfUnecessaryMetricsEnabled(boolean recordingOfUnecessaryMetricsEnabled) {
		this.recordingOfUnecessaryMetricsEnabled = recordingOfUnecessaryMetricsEnabled;
	}

	public MetricConfiguration() {
	}
	
	public static MetricConfiguration read(String serializedValue) throws JsonMappingException, JsonProcessingException, IOException {
		return Environment.deserializeObjectFromJson(serializedValue, MetricConfiguration.class);
	}
	
	public void filter(String excludedCategories) {
		if(metricDefinitions == null) return;
		
		List<MetricProductCategory> categoriesToExclude = Arrays.stream(excludedCategories.split(","))
				.map(s -> MetricProductCategory.valueOf(s))
				.collect(Collectors.toList());
		
		setMetricDefinitions(metricDefinitions.stream()
				.filter(d -> ! categoriesToExclude.contains(d.getCategory()))
				.collect(Collectors.toList()));
	}
}
