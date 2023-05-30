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
package com.ibm.decision.metering.ilmt.service.registration.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.slf4j.Logger;

import com.ibm.decision.metering.ilmt.model.registration.RegistrationRequest;
import com.ibm.decision.metering.ilmt.service.util.Environment;
import com.ibm.decision.metering.ilmt.service.util.Messages;


@Entity
@Table(name = "REGISTRATION")
public class RegistrationEntity {
	private final static Logger LOGGER = Environment.getLogger();
	
	@Id	
	@Column(name="ID")
	private String id;
	
	@Column(name="LAST_UPDATE")
	private long lastUpdate;	
	
	@Column(name="DEFINITION")
	private String definition;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public RegistrationEntity() {
	}

	public RegistrationEntity(String id, String definition) {
		this.id = id;
		this.definition = definition;
	}
	
	public RegistrationRequest toRegistrationRequest() {
		try {
			return Environment.deserializeObjectFromJson(definition, RegistrationRequest.class);
		}
		catch(Exception e) {
			LOGGER.error(Environment.getMessage(Messages.GENERIC_ERROR), e);
		}
		return null;
	}
	
	public static RegistrationEntity fromRegistrationRequest(RegistrationRequest registration) {
		try {
			String definition = Environment.serializeObjectToJson(registration);
			return new RegistrationEntity(registration.getInstanceIdentifier(), definition);
		}
		catch(Exception e) {
			LOGGER.error(Environment.getMessage(Messages.GENERIC_ERROR), e);
		}
		return null;
	}
}
