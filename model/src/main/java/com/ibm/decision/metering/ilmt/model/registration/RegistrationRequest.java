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

package com.ibm.decision.metering.ilmt.model.registration;

import static com.ibm.decision.metering.ilmt.model.EqualUtil.hasSameValue;
import static com.ibm.decision.metering.ilmt.model.Helper.appendString;
import static com.ibm.decision.metering.ilmt.model.metric.GroupImpl.resetAndResolveParents;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ibm.decision.metering.ilmt.model.MessageHandler;
import com.ibm.decision.metering.ilmt.model.Messages;
import com.ibm.decision.metering.ilmt.model.RequestBase;
import com.ibm.decision.metering.ilmt.model.RequestID;
import com.ibm.decision.metering.ilmt.model.metric.Group;
import com.ibm.decision.metering.ilmt.model.metric.GroupImpl;
import com.ibm.decision.metering.ilmt.model.metric.MetricDefinition;
import com.ibm.decision.metering.ilmt.model.metric.MetricDefinitionImpl;

/**
 * JSON payload for POST /api/startup REST API
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationRequest extends RequestBase {

	private long startTime;

	private long sendTime;

	private String adminUiUrl;

	private List<Product> products = new ArrayList<Product>();

	private String operatingSystem = System.getProperty("os.name"); // No_i18n

	private String operatingSystemVersion = System.getProperty("os.version"); // No_i18n

	private List<String> customerTagData = new ArrayList<String>();

	private List<String> cloudServicesUsed = new ArrayList<String>();

	private List<Group> groups = new ArrayList<Group>();

	private List<MetricDefinition> metrics = new ArrayList<MetricDefinition>();

	protected RegistrationRequest() {
	}

	public RegistrationRequest(RequestID requestID) {
		this(requestID, null);
	}

	public RegistrationRequest(RequestID requestID, Product product) {
		this(requestID, product, System.nanoTime());
	}

	public RegistrationRequest(RequestID requestID, Product product,
			long startTime) {
		super(requestID);
		this.startTime = startTime;
		this.sendTime = startTime;
		if (product == null) {
			MessageHandler messages = new MessageHandler();
			throw new InvalidParameterException(
					messages.getSevereMessage(Messages.ERROR_REQUEST_NULL_PRODUCT));
		}
		products.add(product);
	}

	public RegistrationRequest(RegistrationRequest registrationRequest) {
		super(registrationRequest.getRequestID());
		setAdminUiUrl(registrationRequest.getAdminUiUrl());
		setOperatingSystem(registrationRequest.getOperatingSystem());
		setOperatingSystemVersion(registrationRequest
				.getOperatingSystemVersion());
		setSendTime(registrationRequest.getSendTime());
		setStartTime(registrationRequest.getStartTime());
		setProducts(registrationRequest.getProducts());
		setCustomerTagData(registrationRequest.getCustomerTagData());
		setCloudServicesUsed(registrationRequest.getCloudServicesUsed());
		setGroupsAsList(registrationRequest.getGroups());
		setMetricsAsList(registrationRequest.getMetrics());
	}

	public Long getStartTime() {
		return startTime;
	}

	public RegistrationRequest setStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public Long getSendTime() {
		return sendTime;
	}

	public List<Product> getProducts() {
		return products;
	}

	public RegistrationRequest setProducts(List<Product> products) {
		this.products.clear();
		this.products.addAll(products);
		return this;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public String getOperatingSystemVersion() {
		return operatingSystemVersion;
	}

	public String getAdminUiUrl() {
		return adminUiUrl;
	}

	public RegistrationRequest setAdminUiUrl(String adminUiUrl) {
		this.adminUiUrl = adminUiUrl;
		return this;
	}

	public RegistrationRequest setOperatingSystemVersion(String op) {
		this.operatingSystemVersion = op;
		return this;
	}

	public RegistrationRequest setOperatingSystem(String op) {
		this.operatingSystem = op;
		return this;
	}

	public RegistrationRequest setSendTime(Long sendTime) {
		this.sendTime = sendTime;
		return this;
	}

	public List<String> getCustomerTagData() {
		return customerTagData;
	}

	public RegistrationRequest setCustomerTagData(List<String> customerTagData) {
		this.customerTagData.clear();
		this.customerTagData.addAll(customerTagData);
		return this;
	}

	public List<String> getCloudServicesUsed() {
		return cloudServicesUsed;
	}

	public RegistrationRequest setCloudServicesUsed(
			List<String> cloudServicesUsed) {
		this.cloudServicesUsed.clear();
		this.cloudServicesUsed.addAll(cloudServicesUsed);
		return this;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof RegistrationRequest)) {
			return false;
		}
		RegistrationRequest registrationRequest = (RegistrationRequest) object;
		if (!super.equals(registrationRequest)) {
			return false;
		}
		if (!hasSameValue(adminUiUrl, registrationRequest.getAdminUiUrl())) {
			return false;
		}
		if (!hasSameValue(operatingSystem,
				registrationRequest.getOperatingSystem())) {
			return false;
		}
		if (!hasSameValue(operatingSystemVersion,
				registrationRequest.getOperatingSystemVersion())) {
			return false;
		}
		if (startTime != registrationRequest.getStartTime()
				|| sendTime != registrationRequest.getSendTime()) {
			return false;
		}
		if (!hasSameValue(cloudServicesUsed,
				registrationRequest.getCloudServicesUsed())) {
			return false;
		}
		if (!hasSameValue(customerTagData,
				registrationRequest.getCustomerTagData())) {
			return false;
		}
		if (!hasSameValue(groups, registrationRequest.getGroups())) {
			return false;
		}
		if (!hasSameValue(metrics, registrationRequest.getMetrics())) {
			return false;
		}
		return hasSameValue(products, registrationRequest.getProducts());
	}

	@Override
	public int hashCode() {
		return 37
				+ super.hashCode()
				+ (adminUiUrl == null ? 0 : adminUiUrl.hashCode())
				+ (operatingSystem == null ? 0 : operatingSystem.hashCode())
				+ (operatingSystemVersion == null ? 0 : operatingSystemVersion
						.hashCode())
				+ (cloudServicesUsed == null ? 0 : cloudServicesUsed.hashCode())
				+ new Long(startTime).hashCode()
				+ new Long(sendTime).hashCode()
				+ (customerTagData == null ? 0 : customerTagData.hashCode())
				+ (products == null ? 0 : products.hashCode())
				+ (metrics == null ? 0 : metrics.hashCode())
				+ (groups == null ? 0 : groups.hashCode());
	}

	@Override
	public String toString() {
		StringBuilder tmp = toStringInternal().append(" }");
		return tmp.toString();
	}

	void resolveInternalGroupReferences() {
		List<Group> theGroups = getGroups();
		resetAndResolveParents(theGroups);
		for (MetricDefinition metricDefinition : getMetrics()) {
			if (metricDefinition instanceof MetricDefinitionImpl) {
				((MetricDefinitionImpl) metricDefinition).resolveGroupAggregations(theGroups);
			}
		}
	}
	
	@Override
	protected StringBuilder toStringInternal() {
		StringBuilder tmp = super.toStringInternal();
		if (getRequestID() != null) {
			tmp.append(", ");
		}
		tmp.append("\"startTime\" : ").append(startTime);
		tmp.append(", \"sendTime\" : ").append(sendTime);
		tmp.append(", \"adminUiUrl\" : ").append(appendString(adminUiUrl));
		tmp.append(", \"operatingSystem\" : ").append(
				appendString(operatingSystem));
		tmp.append(", \"operatingSystemVersion\" : ").append(
				appendString(operatingSystemVersion));
		tmp.append(", \"cloudServicesUsed\" : ").append(cloudServicesUsed);
		tmp.append(", \"customerTagData\" : ").append(customerTagData);
		tmp.append(", \"products\" : ").append(products);
		tmp.append(", \"groups\" : ").append(groups);
		tmp.append(", \"metrics\" : ").append(metrics);
		return tmp;
	}

	public List<Group> getGroups() {
		return groups;
	}

	@JsonSetter
	@JsonDeserialize(contentAs = GroupImpl.class)
	public RegistrationRequest setGroups(Group... groups) {
		return setGroupsAsList(Arrays.asList(groups));
	}

	@JsonIgnore
	public RegistrationRequest setGroupsAsList(List<Group> groups) {
		this.groups.clear();
		this.groups.addAll(groups);
		return this;
	}

	public List<MetricDefinition> getMetrics() {
		return metrics;
	}

	@JsonSetter
	@JsonDeserialize(contentAs = MetricDefinitionImpl.class)
	public RegistrationRequest setMetrics(MetricDefinition... metrics) {
		return setMetricsAsList(Arrays.asList(metrics));
	}

	@JsonIgnore
	public RegistrationRequest setMetricsAsList(List<MetricDefinition> metrics) {
		this.metrics.clear();
		this.metrics.addAll(metrics);
		return this;
	}
}
