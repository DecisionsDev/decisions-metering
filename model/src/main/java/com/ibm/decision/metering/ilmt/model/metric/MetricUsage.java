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

package com.ibm.decision.metering.ilmt.model.metric;

import com.ibm.decision.metering.ilmt.model.usage.Usage;

public class MetricUsage<T> {

	private final Metric<T, ?> metric;

	private final T usage;

	public MetricUsage(Metric<T, ?> metric, T usage) {
		this.metric = metric;
		this.usage = usage;
	}

	public Metric<T, ?> getMetric() {
		return metric;
	}
	
	@SuppressWarnings("unused")
	private T getUsage() {
		return usage;
	}
	
	public T updateMetric() {
		return metric.updateUsage(usage);
	}

	public Usage toUsage() {
		if (usage instanceof Number) {
			Number number = (Number) usage;
			return new Usage(metric.getName(), number);
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder tmp = new StringBuilder("{ ");
		tmp.append("\"metric\" : ").append(metric);
		tmp.append(", \"usage\" : ").append(usage);
		tmp.append(" }");
		return tmp.toString();
	}
}
