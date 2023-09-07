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

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static com.ibm.decision.metering.ilmt.model.EqualUtil.hasSameValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class AggregatorImpl implements Aggregator {

	private List<AggregationMethod> sliceAggregationMethods = new ArrayList<AggregationMethod>();

	private List<AggregationMethod> rangeAggregationMethods = new ArrayList<AggregationMethod>();

	@Override
	@JsonInclude(NON_DEFAULT)
	public List<AggregationMethod> getSliceAggregationMethods() {
		return sliceAggregationMethods;
	}

	@Override
	@JsonInclude(NON_DEFAULT)
	public List<AggregationMethod> getRangeAggregationMethods() {
		return rangeAggregationMethods;
	}

	@Override
	public AggregatorImpl setSliceAggregationMethods(
			AggregationMethod... sliceAggregationMethods) {
		this.sliceAggregationMethods.clear();
		addSliceAggregationMethods(sliceAggregationMethods);
		return this;
	}

	AggregatorImpl addSliceAggregationMethods(
			AggregationMethod... aggregationMethods) {
		this.sliceAggregationMethods.addAll(Arrays.asList(aggregationMethods));
		return this;
	}

	@Override
	public AggregatorImpl setRangeAggregationMethods(
			AggregationMethod... aggregationMethods) {
		this.rangeAggregationMethods.clear();
		addRangeAggregationMethods(aggregationMethods);
		return this;
	}

	AggregatorImpl addRangeAggregationMethods(
			AggregationMethod... aggregationMethods) {
		this.rangeAggregationMethods.addAll(Arrays.asList(aggregationMethods));
		return this;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof AggregatorImpl)) {
			return false;
		}
		AggregatorImpl aggregatorImpl = (AggregatorImpl) object;
		if (!hasSameValue(sliceAggregationMethods,
				aggregatorImpl.getSliceAggregationMethods())) {
			return false;
		}
		return hasSameValue(rangeAggregationMethods,
				aggregatorImpl.getRangeAggregationMethods());
	}

	@Override
	public int hashCode() {
		return 37 + super.hashCode() + sliceAggregationMethods.hashCode()
				+ +rangeAggregationMethods.hashCode();
	}

	protected StringBuilder toStringInternal() {
		StringBuilder tmp = new StringBuilder("{ ");
		boolean hasRangeAggregationMethods = !rangeAggregationMethods.isEmpty();
		if (!sliceAggregationMethods.isEmpty()) {
			tmp.append("\"sliceAggregationMethods\" : ").append(
					sliceAggregationMethods);
			if (hasRangeAggregationMethods) {
				tmp.append(", ");
			}
		}
		if (hasRangeAggregationMethods) {
			tmp.append("\"rangeAggregationMethods\" : ").append(
					rangeAggregationMethods);
		}
		return tmp;
	}

	@Override
	public String toString() {
		StringBuilder tmp = toStringInternal();
		tmp.append(" }");
		return tmp.toString();
	}
}
