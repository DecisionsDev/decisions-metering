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
package com.ibm.rules.metering.reporting;

public class FixedMetricFormatter implements MetricFormatter {
	
	String metricName;
	
	public FixedMetricFormatter() {
	}
	
	public FixedMetricFormatter(String metricName) {
		this.metricName = metricName;
	}
	
	@Override
	public String getMetricName(double value) {
		return metricName;
	}

	@Override
	public double getMetricValue(double value) {
		return value;
	}

	@Override
	public double getMetricRemainder(double value) {
		return 0;
	}
}

