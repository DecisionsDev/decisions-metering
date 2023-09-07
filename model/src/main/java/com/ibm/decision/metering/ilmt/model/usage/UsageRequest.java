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

package com.ibm.decision.metering.ilmt.model.usage;

import static com.ibm.decision.metering.ilmt.model.EqualUtil.hasSameValue;
import static com.ibm.decision.metering.ilmt.model.Helper.appendString;

import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ibm.decision.metering.ilmt.model.ProductSpecificData;
import com.ibm.decision.metering.ilmt.model.RequestBase;
import com.ibm.decision.metering.ilmt.model.RequestID;

/**
 * JSON payload for invoking the POST /api/usage REST API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsageRequest extends RequestBase {

	private long startTime;

	private long endTime;

	private String environmentType;

	private ProductSpecificData productSpecificData;

	private List<Usage> usageList = new ArrayList<Usage>();

	public UsageRequest() {
		super();
	}

	public UsageRequest(RequestID requestID, long startTime) {
		super(requestID);
		this.startTime = startTime;
	}

	public UsageRequest(RequestID requestID) {
		this(requestID, System.currentTimeMillis());
	}

	public UsageRequest(UsageRequest usageRequest) {
		super(usageRequest.getRequestID());
		setEndTime(usageRequest.getEndTime());
		setStartTime(usageRequest.getStartTime());
		setEnvironmentType(usageRequest.getEnvironmentType());
		setProductSpecificData((usageRequest.getProductSpecificData()));
		setUsageList(usageRequest.getUsageList());
	}

	public long getStartTime() {
		return startTime;
	}

	public UsageRequest setStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public long getEndTime() {
		return endTime;
	}

	public UsageRequest setEndTime(long endTime) {
		this.endTime = endTime;
		return this;
	}

	public ProductSpecificData getProductSpecificData() {
		return productSpecificData;
	}

	public UsageRequest setProductSpecificData(
			ProductSpecificData productSpecificData) {
		this.productSpecificData = productSpecificData;
		return this;
	}

	public List<Usage> getUsageList() {
		return usageList;
	}

	public UsageRequest setUsageList(List<Usage> usages) {
		this.usageList.clear();
		this.usageList.addAll(usages);
		return this;
	}

	public boolean addUsage(Usage usage) {
		return usageList.add(usage);
	}

	public String getEnvironmentType() {
		return environmentType;
	}

	public UsageRequest setEnvironmentType(String environmentType) {
		this.environmentType = environmentType;
		return this;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof UsageRequest)) {
			return false;
		}
		UsageRequest usageRequest = (UsageRequest) object;
		if (!super.equals(object)) {
			return false;
		}
		if (!hasSameValue(environmentType, usageRequest.getEnvironmentType())) {
			return false;
		}
		if (startTime != usageRequest.getStartTime()
				|| endTime != usageRequest.getEndTime()) {
			return false;
		}
		if (!hasSameValue(productSpecificData,
				usageRequest.getProductSpecificData())) {
			return false;
		}
		return hasSameValue(usageList, usageRequest.getUsageList());
	}

	@Override
	public int hashCode() {
		return 37
				+ super.hashCode()
				+ new Long(startTime).hashCode()
				+ new Long(endTime).hashCode()
				+ (environmentType == null ? 0 : environmentType.hashCode())
				+ (usageList == null ? 0 : usageList.hashCode())
				+ (productSpecificData == null ? 0 : productSpecificData
						.hashCode());
	}

	@Override
	public String toString() {
		return toStringInternal().append(" }").toString();
	}

	@Override
	protected StringBuilder toStringInternal() {
		StringBuilder tmp = super.toStringInternal();
		if (getRequestID() != null) {
			tmp.append(", ");
		}
		tmp.append(", \"startTime\" : ").append(startTime);
		tmp.append(", \"endTime\" : ").append(endTime);
		tmp.append(", \"environmentType\" : ").append(
				appendString(environmentType));
		tmp.append(", \"usageList\" : ").append(usageList);
		return tmp.append(", \"productSpecificData\" : ").append(
				productSpecificData);
	}
}
