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

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.ibm.decision.metering.ilmt.model.EqualUtil.hasSameValue;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupImpl implements Group {

	public static void resetAndResolveParents(Group... groups) {
		HashMap<String, Group> groupMap = new HashMap<String, Group>();
		for (Group group : groups) {
			if (group != null) {
				String id = group.getId();
				if (id != null && !id.isEmpty()) {
					groupMap.put(group.getId(), group);
				}
				if (group instanceof GroupImpl) {
					((GroupImpl) group).resetParent();
				}
			}
		}
		for (Group group : groups) {
			if (group != null) {
				String parentGroupId = group.getParentGroupId();
				group.setParent(groupMap.get(parentGroupId));
				group.setParentGroupId(parentGroupId);
			}
		}
	}

	public static void resetAndResolveParents(List<Group> groups) {
		resetAndResolveParents(groups.toArray(new Group[groups.size()]));
	}

	private String id;

	private String name;

	private Map<Locale, String> translatedNames = new HashMap<Locale, String>();

	private Group parent;

	private String parentGroupId;

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof GroupImpl)) {
			return false;
		}
		GroupImpl groupImpl = (GroupImpl) object;
		if (!hasSameValue(id, groupImpl.getId())) {
			return false;
		}
		if (!hasSameValue(name, groupImpl.getName())) {
			return false;
		}
		if (!hasSameValue(translatedNames, groupImpl.getTranslatedName())) {
			return false;
		}
		if (!hasSameValue(parentGroupId, groupImpl.parentGroupId)) {
			return false;
		}
		if (parent == this) {
			return groupImpl == groupImpl.getParent();
		}
		return hasSameValue(parent, groupImpl.getParent());
	}

	@Override
	public int hashCode() {
		return 37 + super.hashCode() + (id == null ? 0 : id.hashCode())
				+ (name == null ? 0 : name.hashCode())
				+ (parentGroupId == null ? 0 : parentGroupId.hashCode())
				+ (parent == null ? 0 : parent.hashCode())
				+ translatedNames.hashCode();
	}

	@Override
	public GroupImpl setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public GroupImpl setTranslatedName(Map<Locale, String> translatedNames) {
		this.translatedNames.clear();
		this.translatedNames.putAll(translatedNames);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder tmp = new StringBuilder("{ ");
		boolean hasId = (id != null) && !id.isEmpty();
		if (hasId) {
			tmp.append("\"id\" : \"").append(id).append("\"");
		}
		boolean hasName = (name != null) && !name.isEmpty();
		if (hasName) {
			if (hasId) {
				tmp.append(", ");
			}
			tmp.append("\"name\" : \"").append(name).append("\"");
		}
		boolean hasTranslatedNames = (translatedNames != null)
				&& !translatedNames.isEmpty();
		if (hasTranslatedNames) {
			if (hasId || hasName) {
				tmp.append(", ");
			}
			tmp.append("\"translatedNames\" : ").append(translatedNames);
		}
		String parentGroupId = getParentGroupId();
		boolean hasParentGroupId = (parentGroupId != null && !parentGroupId.isEmpty());
		if (hasParentGroupId) {
			if (hasId || hasName || hasTranslatedNames) {
				tmp.append(", ");
			}
			tmp.append("\"parentGroupId\" : \"").append(parentGroupId)
					.append("\"");
		}
		tmp.append(" }");
		return tmp.toString();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	@JsonInclude(NON_DEFAULT)
	public Map<Locale, String> getTranslatedName() {
		return translatedNames;
	}

	@Override
	@JsonIgnore
	public Group getParent() {
		return parent;
	}

	@Override
	@JsonInclude(NON_NULL)
	public String getParentGroupId() {
		return parent != null ? parent.getId() : parentGroupId;
	}

	@Override
	public GroupImpl setId(String id) {
		this.id = id;
		return this;
	}

	private void resetParent() {
		this.parent = null;
	}
	
	@Override
	@JsonIgnore
	public GroupImpl setParent(Group parent) {
		this.parent = parent;
		return setParentGroupId(parent == null ? null : parent.getId());
	}

	@Override
	public GroupImpl setParentGroupId(String parentGroupId) {
		this.parentGroupId = parentGroupId;
		return this;
	}
}
