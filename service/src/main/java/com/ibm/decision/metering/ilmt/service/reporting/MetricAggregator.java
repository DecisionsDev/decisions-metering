/*
 *
 *   Copyright IBM Corp. 2024
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

import java.util.HashMap;
import java.util.Map;

import com.ibm.decision.metering.ilmt.model.usage.Usage;

public class MetricAggregator {
	Map<MetricIdentification, MetricValue> metricAggregations;

	public Map<MetricIdentification, MetricValue> getMetricAggregations() {
		return metricAggregations;
	}

	public void aggregateValue(MetricIdentification metricIdentification, Usage usage, long usageDate) {
		MetricValue currentValue = metricAggregations.get(metricIdentification);
		
		if(currentValue == null) {
			metricAggregations.put(metricIdentification, new MetricValue(usage.getMetricValue().doubleValue(), usageDate));
		}
		else {
			switch(metricIdentification.getMetricDefinition().getAggregationMethod()) {
				case SUM:
					Double total = currentValue.getValue() + usage.getMetricValue().doubleValue();
					currentValue.setValue(total);
					currentValue.setValueDate(usageDate);
					break;
				case LATEST:
					if(usageDate > currentValue.getValueDate()) {
						currentValue.setValue(usage.getMetricValue().doubleValue());
						currentValue.setValueDate(usageDate);
					}
					break;
				case MAX:
					if(usage.getMetricValue().doubleValue() > currentValue.getValue()) {
						currentValue.setValue(usage.getMetricValue().doubleValue());
						currentValue.setValueDate(usageDate);
					}
					break;
				default:
					break;
			}
		}
	}
	
	public double aggregateRemainder(MetricIdentification metricIdentification, double remainder) {
		MetricValue currentValue = metricAggregations.get(metricIdentification);
		
		if(metricIdentification.getMetricDefinition().getAggregationMethod() == MetricAggregationMethod.SUM) {	
			currentValue.setValue(currentValue.getValue() + remainder);
		}
		
		return currentValue.getValue();
	}

	public MetricAggregator() {
		metricAggregations = new HashMap<MetricIdentification, MetricValue>();
	}
}
