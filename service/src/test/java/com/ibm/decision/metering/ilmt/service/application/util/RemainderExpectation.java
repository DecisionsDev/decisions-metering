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
package com.ibm.decision.metering.ilmt.service.application.util;

public class RemainderExpectation {
	String metricName;
	double expectedValue;
	int expectedStatus;
	
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
	public int getExpectedStatus() {
		return expectedStatus;
	}
	public void setExpectedStatus(int expectedStatus) {
		this.expectedStatus = expectedStatus;
	}
	
	public RemainderExpectation(String metricName, double expectedValue, int expectedStatus) {
		this.metricName = metricName;
		this.expectedValue = expectedValue;
		this.expectedStatus = expectedStatus;
	}
}
