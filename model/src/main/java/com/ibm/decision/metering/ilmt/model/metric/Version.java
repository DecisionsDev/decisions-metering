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

public enum Version {

	PREVIOUS_VERSION("PREVIOUS VERSION"), 
	CURRENT_VERSION("8.9.0-SNAPSHOT"); // No_i18n

	private final String versionId;

	private Version(String versionId) {
		this.versionId = versionId;
	}

	public String getId() {
		return versionId;
	}

	public boolean isDefault() {
		return this == getDefault();
	}

	public static Version getDefault() {
		return CURRENT_VERSION;
	}

	@Override
	public String toString() {
		if (versionId != null) {
			return versionId;
		}
		return super.toString();
	}

	public static Version getVersionFromId(String versionId) {
		for (Version version : values()) {
			if (version.getId().equals(versionId)) {
				return version;
			}
		}
		return null;
	}
}
