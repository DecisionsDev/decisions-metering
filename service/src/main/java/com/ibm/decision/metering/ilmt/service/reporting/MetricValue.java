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
package com.ibm.decision.metering.ilmt.service.reporting;

public class MetricValue {
	private Double value;
	private long valueDate;
	private Long oldestDate;
	private Long newestDate;
	
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public long getValueDate() {
		return valueDate;
	}
	public void setValueDate(long valueDate) {
		updateDates(valueDate);
	}
	
	public Long getOldestDate() {
		return oldestDate;
	}
	public Long getNewestDate() {
		return newestDate;
	}
	public MetricValue() {
	}
	
	public MetricValue(Double value, long valueDate) {
		this.value = value;
		updateDates(valueDate);
	}
	
	private void updateDates(long valueDate) {
		this.valueDate = valueDate;
		if(this.oldestDate == null || valueDate < this.oldestDate) this.oldestDate = valueDate;
		if(this.newestDate == null || valueDate > this.newestDate) this.newestDate = valueDate;
	}
}
