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
package com.ibm.decision.metering.ilmt.service.reporting;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.ibm.license.metric.InstanceDetails;
import com.ibm.license.metric.LicenseMetricLogger;
import com.ibm.license.metric.LoggerConfiguration;
import com.ibm.license.metric.Metric;
import com.ibm.license.metric.SoftwareIdentity;

public class DecisionMeteringReport implements Serializable {
	private static final long serialVersionUID = -161852335256111420L;
	private LocalDateTime startDateTime;
	private LocalDateTime stopDateTime;
	private String persistentId;
	private String softwarePath;
	private MetricFormatter metricFormatter;
	private Double metricValue;
	private String softwareName;
	private String reportOutputDirectory;

	public DecisionMeteringReport() {
	}

	public DecisionMeteringReport(String softwareName, String persistentId, String softwarePath, MetricFormatter metricFormatter, Double metricValue, String reportOutputDirectory) {
		this.softwareName = softwareName;
		this.persistentId = persistentId;
		this.softwarePath = softwarePath;
		this.metricFormatter = metricFormatter;
		this.metricValue = metricValue;
		this.reportOutputDirectory = reportOutputDirectory;
		
		LocalDateTime timePoint = LocalDateTime.now();
		this.startDateTime = timePoint;
		this.stopDateTime = timePoint;
	}
	
	
	public DecisionMeteringReport(String softwareName, String persistentId, String softwarePath, MetricFormatter metricFormatter, Double metricValue, Long startTime, Long endTime, String reportOutputDirectory) {
		this.softwareName = softwareName;
		this.persistentId = persistentId;
		this.softwarePath = softwarePath;
		this.metricFormatter = metricFormatter;
		this.metricValue = metricValue;
		this.reportOutputDirectory = reportOutputDirectory;
		
		LocalDateTime timePoint = LocalDateTime.now();
		if(startTime != null) this.startDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault());
		else this.startDateTime = timePoint;
		
		if(endTime != null) this.stopDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault());
		else this.stopDateTime = timePoint;
	}

	//@JsonIgnore
	public LocalDateTime getStartTimeStamp() {
		return startDateTime;
	}

	//@JsonIgnore
	public void setStartTimeStamp(LocalDateTime timeStamp) {
		this.startDateTime = timeStamp;
	}

	//@JsonIgnore
	public LocalDateTime getStopTimeStamp() {
		return stopDateTime;
	}

	//@JsonIgnore
	public void setStopTimeStamp(LocalDateTime timeStamp) {
		this.stopDateTime = timeStamp;
	}

	public void setStopTimeStamp() {
		LocalDateTime timePoint = LocalDateTime.now();
		this.stopDateTime = timePoint;
	}
	
	public MetricFormatter getMetricFormatter() {
		return metricFormatter;
	}

	public void setMetricFormatter(MetricFormatter metricFormatter) {
		this.metricFormatter = metricFormatter;
	}

	public Double getMetricValue() {
		return metricValue;
	}

	public void setMetricValue(Double metricValue) {
		this.metricValue = metricValue;
	}

	public String getSoftwareName() {
		return softwareName;
	}

	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

	public String getReportOutputDirectory() {
		return reportOutputDirectory;
	}

	public void setReportOutputDirectory(String reportOutputDirectory) {
		this.reportOutputDirectory = reportOutputDirectory;
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

	public void writeILMTFile() throws Exception {
		SoftwareIdentity softwareIdentity = new SoftwareIdentity(this.persistentId, this.softwareName, new InstanceDetails(this.softwarePath));
		
		LoggerConfiguration configuration = new LoggerConfiguration(reportOutputDirectory, 102400, 10);
		
		LicenseMetricLogger.setLoggerConfiguration(configuration);
		LicenseMetricLogger logger = LicenseMetricLogger.getLogger(softwareIdentity);
		
		Date startDate = Date.from(this.getStartTimeStamp().atZone(ZoneId.systemDefault()).toInstant());
		Date stopDate = Date.from(this.getStopTimeStamp().atZone(ZoneId.systemDefault()).toInstant());

		Metric executionMetric = new Metric(metricFormatter.getMetricName(metricValue), "", metricFormatter.getMetricValue(metricValue), startDate, stopDate); 
		logger.log(executionMetric);
	}
	
	public double getReportedMetricValue() {
		return metricFormatter.getMetricValue(metricValue);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
