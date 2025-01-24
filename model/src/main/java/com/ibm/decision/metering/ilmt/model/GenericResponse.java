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

package com.ibm.decision.metering.ilmt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.List;

import static com.ibm.decision.metering.ilmt.model.EqualUtil.assertNotNull;
import static com.ibm.decision.metering.ilmt.model.EqualUtil.hasSameValue;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericResponse implements StateValidator {

	private String type;
	private int status;
	private String message;
	private List<?> details;

	public GenericResponse(String type, int status, String message) {
		this(type, status, message, null);
	}

	public GenericResponse(String type, int status, String message, List<?> details) {
		setType(type);
		setMessage(message);
		setStatus(status);
		setDetails(details);
	}

	/**
	 * Required for Jackson serialisation
	 */
	GenericResponse() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public GenericResponse setMessage(String message) {
		this.message = message;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public GenericResponse setStatus(int status) {
		this.status = status;
		return this;
	}

	public List<?> getDetails() {
		return details;
	}

	public GenericResponse setDetails(List<?> details) {
		this.details = details;
		return this;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof GenericResponse)) {
			return false;
		}
		GenericResponse genericErrorResponse = (GenericResponse) object;
		if (!hasSameValue(type, genericErrorResponse.getType())) {
			return false;
		}
		if (!hasSameValue(details, genericErrorResponse.getDetails())) {
			return false;
		}
		return hasSameValue(message, genericErrorResponse.getMessage());
	}

	@Override
	public int hashCode() {
		return super.hashCode() + (type == null ? 0 : type.hashCode())
				+ (message == null ? 0 : message.hashCode());
	}

	@Override
	public String toString() {
		StringBuffer tmp = new StringBuffer("{ \"type\" = \"");
		tmp.append(type).append("\", message = \"");
		tmp.append(message);
		if (details != null) {
			tmp.append("" + "\", details = ").append(Arrays.asList(details));
		}
		tmp.append("\" }");
		return tmp.toString();
	}

	@Override
	public void validateState() {
		assertNotNull(getType(), Messages.ERROR_NULL_TYPE_IN_PARSED_ERROR);
		assertNotNull(getMessage(), Messages.ERROR_NULL_MESSAGE_IN_PARSED_ERROR);
	}

}
