/*
 *
 *   Copyright IBM Corp. 2024
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
package com.ibm.decision.metering.ilmt.service.usage.repository;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.slf4j.Logger;

import com.ibm.decision.metering.ilmt.model.usage.UsageRequest;
import com.ibm.decision.metering.ilmt.service.util.Environment;
import com.ibm.decision.metering.ilmt.service.util.Messages;

@Entity
@Table(name = "USAGE")
public class UsageEntity {
	private final static Logger LOGGER = Environment.getLogger();
	
	@Id
	@Column(name="ID")
	private String id;
	
	@Column(name="INSTANCE_ID")
	private String instanceId;
	
	@Column(name="TIMESTAMP")
	private long timestamp;
	
	@Column(name="DEFINITION")
	private String definition;
	
	@Column(name="REPORT")
	private String report;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public UsageEntity() {
	}

	public UsageEntity(String instanceId, String definition) {
		this.id = UUID.randomUUID().toString();
		this.instanceId = instanceId;
		this.definition = definition;
		this.timestamp = Instant.now().toEpochMilli();
	}
	
	public UsageRequest toUsageRequest() {
		try {
			return Environment.deserializeObjectFromJson(definition, UsageRequest.class);
		}
		catch(Exception e) {
			LOGGER.error(Environment.getMessage(Messages.GENERIC_ERROR), e);
		}
		return null;
	}
	
	public static UsageEntity fromUsageRequest(UsageRequest usage) {
		try {
			String definition = Environment.serializeObjectToJson(usage);
			return new UsageEntity(usage.getInstanceIdentifier(), definition);
		}
		catch(Exception e) {
			LOGGER.error(Environment.getMessage(Messages.GENERIC_ERROR), e);
		}
		return null;
	}
}
