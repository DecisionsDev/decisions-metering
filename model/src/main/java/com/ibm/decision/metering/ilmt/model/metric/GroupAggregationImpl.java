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

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.ibm.decision.metering.ilmt.model.EqualUtil.hasSameValue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;



@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupAggregationImpl extends AggregatorImpl implements
		GroupAggregation {

	private String groupId;

	private Group group;

	private AggregationMethod rollUp;

	void resolveGroup(Group... groups) {
		if (groupId != null && !groupId.isEmpty()) {
			for (Group group : groups) {
				if (groupId.equals(group.getId())) {
					setGroup(group);
				}
			}
		}
	}

	@Override
	public boolean equals(Object object) {
		if (!super.equals(object)) {
			return false;
		}
		if (!(object instanceof GroupAggregationImpl)) {
			return false;
		}
		GroupAggregationImpl aggregatorImpl = (GroupAggregationImpl) object;
		if (!hasSameValue(groupId, aggregatorImpl.groupId)) {
			return false;
		}
		if (!hasSameValue(group, aggregatorImpl.getGroup())) {
			return false;
		}
		return hasSameValue(rollUp, aggregatorImpl.getRollUp());
	}

	@Override
	public int hashCode() {
		return 37 + super.hashCode() + (group == null ? 0 : group.hashCode())
				+ (groupId == null ? 0 : groupId.hashCode())
				+ + +(rollUp == null ? 0 : rollUp.hashCode());
	}

	@Override
	public String toString() {
		StringBuilder tmp = toStringInternal();
		boolean hasPreviousFields = !getRangeAggregationMethods().isEmpty()
				|| !getSliceAggregationMethods().isEmpty();
		if (!getRangeAggregationMethods().isEmpty()
				|| !getSliceAggregationMethods().isEmpty()) {
		}
		boolean hasGroupId = (getGroupId() != null);
		if (hasGroupId) {
			if (hasPreviousFields) {
				tmp.append(", ");
			}
			tmp.append("\"groupId\" : \"").append(getGroupId()).append("\"");
		}
		hasPreviousFields = hasGroupId || hasPreviousFields;
		boolean hasRollUp = (rollUp != null);
		if (hasRollUp) {
			if (hasPreviousFields) {
				tmp.append(", ");
			}
			tmp.append("\"rollUp\" : ").append(rollUp);
		}
		tmp.append(" }");
		return tmp.toString();
	}

	@Override
	@JsonInclude(NON_NULL)
	public AggregationMethod getRollUp() {
		return rollUp;
	}

	@Override
	@JsonIgnore
	public GroupAggregationImpl setGroup(Group group) {
		this.group = group;
		return setGroupId(group.getId());
	}

	@Override
	public GroupAggregationImpl setGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	@Override
	public GroupAggregationImpl setRollUp(AggregationMethod aggregationMethod) {
		this.rollUp = aggregationMethod;
		return this;
	}

	@Override
	@JsonIgnore
	public Group getGroup() {
		return group;
	}

	@Override
	@JsonInclude(NON_NULL)
	public String getGroupId() {
		return group == null ? groupId : group.getId();
	}

	@Override
	GroupAggregationImpl addSliceAggregationMethods(
			AggregationMethod... aggregationMethods) {
		super.addSliceAggregationMethods(aggregationMethods);
		return this;
	}

	@Override
	GroupAggregationImpl addRangeAggregationMethods(
			AggregationMethod... aggregationMethods) {
		super.addRangeAggregationMethods(aggregationMethods);
		return this;
	}

	@Override
	public GroupAggregationImpl setSliceAggregationMethods(
			AggregationMethod... aggregationMethods) {
		super.setSliceAggregationMethods(aggregationMethods);
		return this;
	}

	@Override
	public GroupAggregationImpl setRangeAggregationMethods(
			AggregationMethod... aggregationMethods) {
		super.setRangeAggregationMethods(aggregationMethods);
		return this;
	}
}
