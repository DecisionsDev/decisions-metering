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

package com.ibm.decision.metering.ilmt.model.metric;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface MetricDefinition extends Aggregator, TranslatedName,
		TranslatedUnits {

	String getMetricType();
	
	List<GroupAggregation> getGroupAggregations();

	MetricDefinition setMetricType(String metricType);

	Number getMin();

	MetricDefinition setMin(Number min);

	@Override
	MetricDefinition setName(String name);

	@Override
	MetricDefinition setTranslatedName(Map<Locale, String> translatedNames);

	MetricDefinition setGroupAggregations(
			GroupAggregation... groupAggregations);

	@Override
	MetricDefinition setSliceAggregationMethods(
			AggregationMethod... aggregationMethods);

	@Override
	MetricDefinition setRangeAggregationMethods(
			AggregationMethod... aggregationMethods);

	@Override
	MetricDefinition setUnits(String units);

	@Override
	MetricDefinition setTranslatedUnits(Map<Locale, String> translatedUnits);
}
