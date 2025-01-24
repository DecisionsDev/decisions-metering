/*
 *
 *   Copyright IBM Corp. 2025
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

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static com.ibm.decision.metering.ilmt.model.EqualUtil.hasSameValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricDefinitionImpl extends AggregatorImpl implements
		MetricDefinition {

	private String metricType;

	private String name;

	private Map<Locale, String> translatedNames = new HashMap<Locale, String>();

	private List<GroupAggregation> groupAggregations = new ArrayList<GroupAggregation>();

	private Number min;

	private Map<Locale, String> translatedUnits = new HashMap<Locale, String>();

	private String units;

	public MetricDefinitionImpl() {
		this(null);
	}

	public void resolveGroupAggregations(Group... groups) {
		for (GroupAggregation groupAggregation : groupAggregations) {
			if (groupAggregation instanceof GroupAggregationImpl) {
				((GroupAggregationImpl) groupAggregation).resolveGroup(groups);
			}
		}
	}
	
	public void resolveGroupAggregations(List<Group> groups) {
		resolveGroupAggregations(groups.toArray(new Group[groups.size()]));
	}
	
	public MetricDefinitionImpl(String metricType) {
		setMetricType(metricType);
	}

	@Override
	public String getMetricType() {
		return metricType;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	MetricDefinitionImpl addSliceAggregationMethods(
			AggregationMethod... aggregationMethods) {
		super.addSliceAggregationMethods(aggregationMethods);
		return this;
	}

	@Override
	MetricDefinitionImpl addRangeAggregationMethods(
			AggregationMethod... aggregationMethods) {
		super.addRangeAggregationMethods(aggregationMethods);
		return this;
	}

	@Override
	@JsonInclude(NON_DEFAULT)
	public Map<Locale, String> getTranslatedName() {
		return translatedNames;
	}

	@Override
	public List<GroupAggregation> getGroupAggregations() {
		return groupAggregations;
	}

	@Override
	public MetricDefinitionImpl setMetricType(String metricType) {
		this.metricType = metricType;
		return this;
	}

	@Override
	public MetricDefinitionImpl setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public MetricDefinitionImpl setTranslatedName(
			Map<Locale, String> translatedNames) {
		this.translatedNames.clear();
		this.translatedNames.putAll(translatedNames);
		return this;
	}

	@Override
	@JsonDeserialize(contentAs = GroupAggregationImpl.class)
	public MetricDefinitionImpl setGroupAggregations(
			GroupAggregation... groupAggregations) {
		this.groupAggregations.clear();
		addGroupAggregations(groupAggregations);
		return this;
	}

	MetricDefinitionImpl addGroupAggregations(
			GroupAggregation... groupAggregations) {
		this.groupAggregations.addAll(Arrays.asList(groupAggregations));
		return this;
	}

	@Override
	public MetricDefinitionImpl setSliceAggregationMethods(
			AggregationMethod... aggregationMethods) {
		super.setSliceAggregationMethods(aggregationMethods);
		return this;
	}

	@Override
	public MetricDefinitionImpl setRangeAggregationMethods(
			AggregationMethod... aggregationMethods) {
		super.setRangeAggregationMethods(aggregationMethods);
		return this;
	}

	@Override
	public String getUnits() {
		return units;
	}

	@Override
	@JsonInclude(NON_DEFAULT)
	public Map<Locale, String> getTranslatedUnits() {
		return translatedUnits;
	}

	@Override
	public MetricDefinitionImpl setTranslatedUnits(
			Map<Locale, String> translatedUnits) {
		this.translatedUnits.clear();
		this.translatedUnits.putAll(translatedUnits);
		return this;
	}

	@Override
	public Number getMin() {
		return min;
	}

	@Override
	public MetricDefinitionImpl setMin(Number min) {
		this.min = min;
		return this;
	}

	@Override
	public MetricDefinitionImpl setUnits(String units) {
		this.units = units;
		return this;
	}

	@Override
	public boolean equals(Object object) {
		if (!super.equals(object)) {
			return false;
		}
		if (!(object instanceof MetricDefinitionImpl)) {
			return false;
		}
		MetricDefinitionImpl metricDefinitionImpl = (MetricDefinitionImpl) object;
		if (!hasSameValue(metricType, metricDefinitionImpl.getMetricType())) {
			return false;
		}
		if (!hasSameValue(name, metricDefinitionImpl.getName())) {
			return false;
		}
		if (!hasSameValue(units, metricDefinitionImpl.getUnits())) {
			return false;
		}
		if (!hasSameValue(min, metricDefinitionImpl.getMin())) {
			return false;
		}
		if (!hasSameValue(translatedNames,
				metricDefinitionImpl.getTranslatedName())) {
			return false;
		}
		if (!hasSameValue(translatedUnits,
				metricDefinitionImpl.getTranslatedUnits())) {
			return false;
		}
		return hasSameValue(groupAggregations,
				metricDefinitionImpl.getGroupAggregations());
	}

	@Override
	public int hashCode() {
		return 37
				+ super.hashCode()
				+ (metricType == null ? 0 : metricType.hashCode())
				+ (name == null ? 0 : name.hashCode())
				+ (min == null ? 0 : min.hashCode())
				+ (translatedNames == null ? 0 : translatedNames.hashCode())
				+ (translatedUnits == null ? 0 : translatedUnits.hashCode())
				+ (groupAggregations == null ? 0 : groupAggregations.hashCode())
				+ (units == null ? 0 : units.hashCode());
	}

	@Override
	public String toString() {
		StringBuilder tmp = toStringInternal();
		boolean hasPreviousFields = !getRangeAggregationMethods().isEmpty()
				|| !getSliceAggregationMethods().isEmpty();
		boolean hasMetricType = (metricType != null && !metricType.isEmpty());
		if (hasMetricType) {
			if (hasPreviousFields) {
				tmp.append(", ");
			}
			tmp.append("\"metricType\" : \"").append(getMetricType())
					.append("\"");
		}
		hasPreviousFields = hasMetricType || hasPreviousFields;
		boolean hasName = (name != null && !name.isEmpty());
		if (hasName) {
			if (hasPreviousFields) {
				tmp.append(", ");
			}
			tmp.append("\"name\" : \"").append(getName()).append("\"");
		}
		hasPreviousFields = hasName || hasPreviousFields;
		boolean hasMin = (min != null);
		if (hasMin) {
			if (hasPreviousFields) {
				tmp.append(", ");
			}
			tmp.append("\"min\" : ").append(getMin());
		}
		hasPreviousFields = hasMin || hasPreviousFields;
		boolean hasUnits = (units != null && !units.isEmpty());
		if (hasUnits) {
			if (hasPreviousFields) {
				tmp.append(", ");
			}
			tmp.append("\"units\" : \"").append(getUnits()).append("\"");
		}
		hasPreviousFields = hasUnits || hasPreviousFields;
		boolean hasTranslatedNames = (translatedNames != null && !translatedNames
				.isEmpty());
		if (hasTranslatedNames) {
			if (hasPreviousFields) {
				tmp.append(", ");
			}
			tmp.append("\"translatedName\" : ").append(getTranslatedName());
		}
		hasPreviousFields = hasTranslatedNames || hasPreviousFields;
		boolean hasTranslatedUnits = (translatedUnits != null && !translatedUnits
				.isEmpty());
		if (hasTranslatedUnits) {
			if (hasPreviousFields) {
				tmp.append(", ");
			}
			tmp.append("\"translatedUnits\" : ").append(getTranslatedUnits());
		}
		hasPreviousFields = hasTranslatedUnits || hasPreviousFields;
		boolean hasGroupAggregations = (groupAggregations != null && !groupAggregations
				.isEmpty());
		if (hasGroupAggregations) {
			if (hasPreviousFields) {
				tmp.append(", ");
			}
			tmp.append("\"groupAggregations\" : ").append(
					getGroupAggregations());
		}
		tmp.append(" }");
		return tmp.toString();
	}
}
