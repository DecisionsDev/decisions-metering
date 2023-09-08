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

package com.ibm.decision.metering.ilmt.model;

import static com.ibm.decision.metering.ilmt.model.EqualUtil.hasSameValue;

import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * JSON payload for POST /api/startup REST API
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestBase {

	private RequestID requestID;

	protected RequestBase() {
		requestID = new RequestID();
	}

	public RequestBase(RequestID requestID) {
		this.requestID = requestID;
	}

	public RequestBase(RequestBase request) {
		setRequestID(request.getRequestID());
	}

	@JsonIgnore
	public RequestID getRequestID() {
		return requestID;
	}

	protected void setRequestID(RequestID requestID) {
		this.requestID = requestID;
	}

	@JsonProperty
	public String getInstanceIdentifier() {
		return requestID.getInstanceIdentifier();
	}

	public RequestBase setInstanceIdentifier(String instanceId) {
		requestID.setInstanceIdentifier(instanceId);
		return this;
	}

	public String getInstallDirectory() {
		return requestID.getInstallDirectory();
	}

	public RequestBase setInstallDirectory(String installDirectory) {
		requestID.setInstallDirectory(installDirectory);
		return this;
	}

	public String getHostName() {
		return requestID.getHostName();
	}

	public RequestBase setHostName(String hostName) {
		requestID.setHostName(hostName);
		return this;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof RequestBase)) {
			return false;
		}
		RequestBase registrationRequest = (RequestBase) object;
		return hasSameValue(requestID, registrationRequest.getRequestID());
	}

	@Override
	public int hashCode() {
		return 37 + (requestID == null ? 0 : requestID.hashCode());
	}

	@Override
	public String toString() {
		StringBuilder tmp = toStringInternal().append(" }");
		return tmp.toString();
	}

	@JsonIgnore
	public String getID() throws UnsupportedEncodingException {
		return requestID.getID();
	}

	protected StringBuilder toStringInternal() {
		return requestID != null ? requestID.toStringInternal()
				: new StringBuilder("{ ");
	}
}
