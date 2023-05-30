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
package com.ibm.decision.metering.ilmt.service.metric.repository;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "METRIC")
public class MetricEntity {
	@Id	
	@Column(name="ID")
	private String id;
	
	@Column(name="PERSISTENT_ID")
	private String persistentId;
	
	@Column(name="SOFTWARE_PATH")
	private String softwarePath;
	
	@Column(name="METRIC_NAME")
	private String metricName;
	
	@Column(name="REMAINDER")
	private double remainder = 0;
	
	@Column(name="REMAINDER_STATUS")
	private int remainderStatus = RemainderStatus.NO_PROCESSING_NEEDED;
	
	@Column(name="LAST_UPDATE")
	private long lastUpdateDate;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPersistentId() {
		return persistentId;
	}

	public void setPersistentId(String persistentId) {
		this.persistentId = persistentId;
	}

	public String getSoftwarePath() {
		return softwarePath;
	}

	public void setSoftwarePath(String softwarePath) {
		this.softwarePath = softwarePath;
	}

	public String getMetricName() {
		return metricName;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public double getRemainder() {
		return remainder;
	}

	public void setRemainder(double remainder) {
		this.remainder = remainder;
	}

	public int getRemainderStatus() {
		return remainderStatus;
	}

	public void setRemainderStatus(int remainderStatus) {
		this.remainderStatus = remainderStatus;
	}

	public long getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(long lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public MetricEntity(){
		this.id = UUID.randomUUID().toString();
	}
}
