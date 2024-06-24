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

import com.ibm.decision.metering.ilmt.model.ProductSpecificData;

import java.util.ArrayList;
import java.util.List;

import static com.ibm.decision.metering.ilmt.model.EqualUtil.hasSameValue;

/**
 * The Class Product is part of the model that is serialized in JSON.
 */
public class Product {

	private String name;

	private String persistentId;

	private String version;

	private List<String> apar = new ArrayList<String>();

	private ProductSpecificData productSpecificData;

	Product() {
		super();
	}

	public Product(String persistentId, String name) {
		this(persistentId, name, null);
	}

	public Product(String persistentId, String name, String version) {
		this.persistentId = persistentId;
		this.name = name;
		this.version = version;
	}

	public String getPersistentId() {
		return persistentId;
	}

	public void setPersistentId(String persistentId) {
		this.persistentId = persistentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public Product setVersion(String version) {
		this.version = version;
		return this;
	}

	public List<String> getApar() {
		return apar;
	}

	public ProductSpecificData getProductSpecificData() {
		return productSpecificData;
	}

	public Product setProductSpecificData(
			ProductSpecificData productSpecificData) {
		this.productSpecificData = productSpecificData;
		return this;
	}

	public Product setApar(List<String> apar) {
		this.apar = apar;
		return this;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Product)) {
			return false;
		}
		Product product = (Product) object;
		if (!hasSameValue(name, product.getName())) {
			return false;
		}
		if (!hasSameValue(persistentId, product.getPersistentId())) {
			return false;
		}
		if (!hasSameValue(version, product.getVersion())) {
			return false;
		}
		if (!hasSameValue(productSpecificData, product.getProductSpecificData())) {
			return false;
		}
		return hasSameValue(apar, product.getApar());
	}

	@Override
	public int hashCode() {
		return 37
				+ (persistentId == null ? 0 : persistentId.hashCode())
				+ (name == null ? 0 : name.hashCode())
				+ (version == null ? 0 : version.hashCode())
				+ (productSpecificData == null ? 0 : productSpecificData
						.hashCode())
				+ (apar == null ? 0 : apar.hashCode())
				+ (productSpecificData == null ? 0 : productSpecificData
						.hashCode());
	}

	@Override
	public String toString() {
		StringBuffer tmp = new StringBuffer("{ \"persistentId\" : \"");
		tmp.append(persistentId).append("\", \"name\" : \"");
		tmp.append(name).append("\", \"version\" : \"");
		tmp.append(version).append("\", \"productSpecificData\" : ");
		tmp.append(productSpecificData).append(", \"apar\" : ");
		tmp.append(apar).append(" }");
		return tmp.toString();
	}
}
