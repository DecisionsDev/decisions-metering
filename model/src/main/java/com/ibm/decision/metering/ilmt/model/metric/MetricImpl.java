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

import static com.ibm.decision.metering.ilmt.model.EqualUtil.hasSameValue;
import static com.ibm.decision.metering.ilmt.model.Helper.appendString;
import static com.ibm.decision.metering.ilmt.model.Messages.ITEM_SEPARATOR;

import java.util.Arrays;
import java.util.Comparator;

import com.ibm.decision.metering.ilmt.model.MessageHandler;

public abstract class MetricImpl<T, P> implements Metric<T, P> {

	private static final String VERSION_SUFFIX = " V_"; // No_i18n

	private static MessageHandler messageHandler = new MessageHandler();

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Metric)) {
			return false;
		}
		Metric<?, ?> metric = (Metric<?, ?>) object;
		if (!hasSameValue(getName(), metric.getName())) {
			return false;
		}
		return metric.getVersion() == getVersion();
	}

	@Override
	public String toString() {
		StringBuilder tmp = new StringBuilder("{ ");
		tmp.append("\"name\" : ").append(appendString(getName()));
		tmp.append(", \"version\" : ").append(getVersion());
		tmp.append(" }");
		return tmp.toString();
	}

	@Override
	public int hashCode() {
		String name = getName();
		Version version = getVersion();
		return 37 + (name == null ? 0 : name.hashCode())
				+ (version == null ? 0 : version.hashCode());
	}

	public static String getNames(boolean appendVersion,
			Metric<?, ?>... metrics) {
		sort(metrics);
		StringBuilder tmp = new StringBuilder();
		String separator = messageHandler.getMessage(ITEM_SEPARATOR);
		for (int metricIndex = 0; metricIndex < metrics.length; metricIndex++) {
			Metric<?, ?> metric = metrics[metricIndex];
			if (metricIndex != 0) {
				tmp.append(separator);
			}
			tmp.append(metric.getName());
			if (appendVersion) {
				tmp.append(VERSION_SUFFIX).append(metric.getVersion());
			}
		}
		return tmp.toString();
	}

	public static void sort(Metric<?, ?>... metrics) {
		Arrays.sort(metrics, new Comparator<Metric<?, ?>>() {

			@Override
			public int compare(Metric<?, ?> lhs, Metric<?, ?> rhs) {
				Version lhsVersion = lhs.getVersion();
				Version rhsVersion = rhs.getVersion();
				if (lhsVersion != rhsVersion) {
					return lhsVersion.getId().compareTo(rhsVersion.getId());
				}
				return lhs.getName().compareTo(rhs.getName());
			}
		});
	}

}
