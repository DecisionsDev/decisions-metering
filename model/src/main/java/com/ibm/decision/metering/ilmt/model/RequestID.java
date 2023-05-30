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


package com.ibm.decision.metering.ilmt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static com.ibm.decision.metering.ilmt.model.EqualUtil.hasSameValue;
import static com.ibm.decision.metering.ilmt.model.Helper.appendString;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestID {

	private static final String QUESTION_MARK = "?"; // No_i18n

	private static final String UTF_8 = "UTF-8"; // No_i18n

	private String instanceIdentifier;

	private String hostName;

	private String installDirectory;

	public String getInstanceIdentifier() {
		return instanceIdentifier;
	}

	public String getHostName() {
		return hostName;
	}

	public String getInstallDirectory() {
		return installDirectory;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof RequestID)) {
			return false;
		}
		RequestID registrationInformation = (RequestID) object;
		if (!hasSameValue(instanceIdentifier,
				registrationInformation.getInstanceIdentifier())) {
			return false;
		}
		if (!hasSameValue(hostName, registrationInformation.getHostName())) {
			return false;
		}
		return hasSameValue(installDirectory,
				registrationInformation.getInstallDirectory());
	}

	@Override
	public int hashCode() {
		return 37
				+ (instanceIdentifier == null ? 0 : instanceIdentifier
						.hashCode())
				+ (installDirectory == null ? 0 : installDirectory.hashCode())
				+ (hostName == null ? 0 : hostName.hashCode());
	}

	public RequestID(String hostName, String installDirectory,
			String instanceIdentifier) {
		this.hostName = hostName;
		this.installDirectory = installDirectory;
		this.instanceIdentifier = instanceIdentifier;
	}

	public RequestID() {
	}

	@Override
	public String toString() {
		StringBuilder tmp = toStringInternal().append("}");
		return tmp.toString();
	}

	StringBuilder toStringInternal() {
		StringBuilder tmp = new StringBuilder("{ ");
		tmp.append("\"instanceIdentifier\" : ").append(
				appendString(instanceIdentifier));
		tmp.append(", \"hostName\" : ").append(appendString(hostName));
		tmp.append(", \"installDirectory\" : ").append(
				appendString(installDirectory));
		return tmp;
	}

	@JsonIgnore
	public String getID() throws UnsupportedEncodingException {
		StringBuffer tmp = new StringBuffer(hostName == null ? "" : hostName);
		tmp.append(getInstallDirectory());
		tmp.append(QUESTION_MARK);
		tmp.append(getInstanceIdentifier());
		return URLEncoder.encode(tmp.toString(), UTF_8);
	}

	public RequestID(String hostName, String installDirectory) {
		this(hostName, installDirectory, null);
	}

	public void setInstanceIdentifier(String instanceIdentifier) {
		this.instanceIdentifier = instanceIdentifier;
	}

	public void setInstallDirectory(String installDirectory) {
		this.installDirectory = installDirectory;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
}
