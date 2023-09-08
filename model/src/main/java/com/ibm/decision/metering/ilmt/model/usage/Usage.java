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

package com.ibm.decision.metering.ilmt.model.usage;

import static com.ibm.decision.metering.ilmt.model.EqualUtil.hasSameValue;

public class Usage {

	private String metricType;

	private Number metricValue;

	public Number getMetricValue() {
		return metricValue;
	}

	public Usage() {
	}

	public Usage(String metricType, Number metricValue) {
		setMetricType(metricType);
		setMetricValue(metricValue);
	}

	public Usage setMetricValue(Number metricValue) {
		this.metricValue = metricValue;
		return this;
	}

	public String getMetricType() {
		return metricType;
	}

	public Usage setMetricType(String metricType) {
		this.metricType = metricType;
		return this;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Usage)) {
			return false;
		}
		Usage usage = (Usage) object;
		if (!hasSameValue(getMetricValue(), usage.getMetricValue())) {
			return false;
		}
		return hasSameValue(getMetricType(), usage.getMetricType());
	}

	@Override
	public int hashCode() {
		return 37 + (metricValue == null ? 0 : metricValue.hashCode())
				+ (metricType == null ? 0 : metricType.hashCode());
	}

	@Override
	public String toString() {
		StringBuffer tmp = new StringBuffer("{ \"metricType\" : \"");
		tmp.append(metricType).append("\", \"metricValue\" : ");
		tmp.append(metricValue).append(" }");
		return tmp.toString();
	}
}
