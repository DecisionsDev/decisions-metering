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
package com.ibm.rules.metering.reporting;

import java.util.Objects;

public class MetricDefinition {
	MetricProductCategory category;
	String originMetricName;
	Double defaultValue;
	String destinationMetricName;
	MetricFormatter formatter;
	MetricAggregationMethod aggregationMethod;
	boolean recordingOfNullUsageEnabled = true;
	boolean reportingNullMetricValueEnabled = false;
	boolean usingRemainders = false;
	
	public MetricProductCategory getCategory() {
		return category;
	}

	public void setCategory(MetricProductCategory category) {
		this.category = category;
	}

	public String getOriginMetricName() {
		return originMetricName;
	}

	public void setOriginMetricName(String originMetricName) {
		this.originMetricName = originMetricName;
	}

	public Double getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Double defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDestinationMetricName() {
		return destinationMetricName;
	}

	public void setDestinationMetricName(String destinationMetricName) {
		this.destinationMetricName = destinationMetricName;
	}

	public MetricFormatter getFormatter() {
		return formatter;
	}

	public void setFormatter(MetricFormatter formatter) {
		this.formatter = formatter;
	}

	public MetricAggregationMethod getAggregationMethod() {
		return aggregationMethod;
	}

	public void setAggregationMethod(MetricAggregationMethod aggregationMethod) {
		this.aggregationMethod = aggregationMethod;
	}

	public boolean isRecordingOfNullUsageEnabled() {
		return recordingOfNullUsageEnabled;
	}

	public void setRecordingOfNullUsageEnabled(boolean recordingOfNullUsageEnabled) {
		this.recordingOfNullUsageEnabled = recordingOfNullUsageEnabled;
	}
	
	public boolean isReportingNullMetricValueEnabled() {
		return reportingNullMetricValueEnabled;
	}

	public void setReportingNullMetricValueEnabled(boolean reportingNullMetricValueEnabled) {
		this.reportingNullMetricValueEnabled = reportingNullMetricValueEnabled;
	}

	public boolean isUsingRemainders() {
		return usingRemainders;
	}

	public void setUsingRemainders(boolean usingRemainders) {
		this.usingRemainders = usingRemainders;
	}

	public MetricDefinition() {
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(! (obj instanceof MetricDefinition)) return false;
		
		MetricDefinition other = (MetricDefinition)obj;
		
		if(category == null && other.getCategory() != null) return false;
		if(category != null && ! category.equals(other.getCategory())) return false;
		
		if(originMetricName == null && other.getOriginMetricName() != null) return false;
		if(originMetricName != null && ! originMetricName.equals(other.getOriginMetricName())) return false;
		
		if(defaultValue == null && other.getDefaultValue() != null) return false;
		if(defaultValue != null && ! defaultValue.equals(other.getDefaultValue())) return false;
		
		if(destinationMetricName == null && other.getDestinationMetricName() != null) return false;
		if(destinationMetricName != null && ! destinationMetricName.equals(other.getDestinationMetricName())) return false;
		
		if(aggregationMethod == null && other.getAggregationMethod() != null) return false;
		if(aggregationMethod != null && ! aggregationMethod.equals(other.getAggregationMethod())) return false;
		
		if(recordingOfNullUsageEnabled != other.isRecordingOfNullUsageEnabled()) return false;
		
		if(reportingNullMetricValueEnabled != other.isReportingNullMetricValueEnabled()) return false;
		
		if(usingRemainders != other.isUsingRemainders()) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getFields());
	}
	
	private Object[] getFields(){
	    return new Object[] { originMetricName, defaultValue, destinationMetricName, aggregationMethod, 
	    		recordingOfNullUsageEnabled, reportingNullMetricValueEnabled };
	}
}