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
package com.ibm.rules.metering.reporting.repository;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "REPORT")
public class ReportEntity {
	@Id	
	@Column(name="ID")
	private String id;
	
	@Column(name="CREATION_DATE")
	private long creationDate;
	
	@Column(name="COMPLETION_DATE")
	private long completionDate;
	
	@Column(name="DEFINITION")
	private String definition;
	
	@Column(name="STATUS")
	private int status;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}

	public long getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(long completionDate) {
		this.completionDate = completionDate;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public ReportEntity() {
		this.id = UUID.randomUUID().toString();
		this.creationDate = Instant.now().toEpochMilli();
		this.status = ReportStatus.CREATED;
	}
}
