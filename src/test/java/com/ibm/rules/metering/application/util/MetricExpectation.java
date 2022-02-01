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
package com.ibm.rules.metering.application.util;

public class MetricExpectation {
	String metricName;
	double expectedValue;
	int unit;
	
	public String getMetricName() {
		return metricName;
	}
	
	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}
	
	public double getExpectedValue() {
		return expectedValue;
	}
	
	public void setExpectedValue(double expectedValue) {
		this.expectedValue = expectedValue;
	}
	
	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	public MetricExpectation(String metricName, double expectedValue, int unit) {
		this.metricName = metricName;
		this.expectedValue = expectedValue;
		this.unit = unit;
	}
}
