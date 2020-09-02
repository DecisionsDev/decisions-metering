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

import java.util.Objects;

public class MetricIdentification {
	private MetricDefinition metricDefinition;
	private String softwarePath;
	private String persistentId;
	
	public MetricDefinition getMetricDefinition() {
		return metricDefinition;
	}

	public void setMetricDefinition(MetricDefinition metricDefinition) {
		this.metricDefinition = metricDefinition;
	}

	public String getSoftwarePath() {
		return softwarePath;
	}

	public void setSoftwarePath(String softwarePath) {
		this.softwarePath = softwarePath;
	}

	public String getPersistentId() {
		return persistentId;
	}

	public void setPersistentId(String persistentId) {
		this.persistentId = persistentId;
	}

	public MetricIdentification() {
	}

	public MetricIdentification(MetricDefinition metricDefinition, String softwarePath, String persistentId) {
		this.metricDefinition = metricDefinition;
		this.softwarePath = softwarePath;
		this.persistentId = persistentId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(! (obj instanceof MetricIdentification)) return false;
		
		MetricIdentification other = (MetricIdentification)obj;
		
		if(metricDefinition == null && other.getMetricDefinition() != null) return false;
		if(metricDefinition != null && ! metricDefinition.equals(other.getMetricDefinition())) return false;
		
		if(softwarePath == null && other.getSoftwarePath() != null) return false;
		if(softwarePath != null && ! softwarePath.equals(other.getSoftwarePath())) return false;
		

		if(persistentId == null && other.getPersistentId() != null) return false;
		if(persistentId != null && ! persistentId.equals(other.getPersistentId())) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getFields());
	}
	
	private Object[] getFields(){
	    return new Object[] { metricDefinition, softwarePath, persistentId };
	}
}
