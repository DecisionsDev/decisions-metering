/*
 *
 *   Copyright IBM Corp. 2020
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
package com.ibm.rules.metering.reporting;

public class ThresholdMetricFormatter implements MetricFormatter {
	double threshold;
	String metricNameOverThreshold;
	String metricNameUnderThreshold;
	double unitOverThreshold;
	double unitUnderThreshold;
	int decimalPrecision = 0;
	ValueApproximation approximation = ValueApproximation.ROUND; 
	
	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public String getMetricNameOverThreshold() {
		return metricNameOverThreshold;
	}

	public void setMetricNameOverThreshold(String metricNameOverThreshold) {
		this.metricNameOverThreshold = metricNameOverThreshold;
	}

	public String getMetricNameUnderThreshold() {
		return metricNameUnderThreshold;
	}

	public void setMetricNameUnderThreshold(String metricNameUnderThreshold) {
		this.metricNameUnderThreshold = metricNameUnderThreshold;
	}

	public double getUnitOverThreshold() {
		return unitOverThreshold;
	}

	public void setUnitOverThreshold(double unitOverThreshold) {
		this.unitOverThreshold = unitOverThreshold;
	}

	public double getUnitUnderThreshold() {
		return unitUnderThreshold;
	}

	public void setUnitUnderThreshold(double unitUnderThreshold) {
		this.unitUnderThreshold = unitUnderThreshold;
	}

	public int getDecimalPrecision() {
		return decimalPrecision;
	}

	public void setDecimalPrecision(int decimalPrecision) {
		this.decimalPrecision = decimalPrecision;
	}

	public ValueApproximation getApproximation() {
		return approximation;
	}

	public void setApproximation(ValueApproximation approximation) {
		this.approximation = approximation;
	}

	public ThresholdMetricFormatter() {
	}

	public ThresholdMetricFormatter(double threshold, String metricNameOverThreshold, String metricNameUnderThreshold,
			double unitOverThreshold, double unitUnderThreshold, int decimalPrecision, ValueApproximation approximation) {
		this.threshold = threshold;
		this.metricNameOverThreshold = metricNameOverThreshold;
		this.metricNameUnderThreshold = metricNameUnderThreshold;
		this.unitOverThreshold = unitOverThreshold;
		this.unitUnderThreshold = unitUnderThreshold;
		this.decimalPrecision = decimalPrecision;
		this.approximation = approximation;
	}

	@Override
	public String getMetricName(double value) {
		if(value >= this.threshold) {
			return this.metricNameOverThreshold;
		}
		
		return this.metricNameUnderThreshold;
	}
	
	@Override
	public double getMetricValue(double value) {
		if(value >= this.threshold) {
			value =  value / unitOverThreshold;
		}
		else {
			value = value / unitUnderThreshold;
		}
		
		return getValueWithDecimalPrecision(value);
	}
	
	private double getAbsoluteMetricValue(double value) {
		double unit = unitOverThreshold;
		if(value < this.threshold) {
			unit = unitUnderThreshold;
		} 
		
		return getMetricValue(value) * unit;
	}
	
	
	@Override
	public double getMetricRemainder(double value) {		
		double approximatedValue = getAbsoluteMetricValue(value);
		return (value > approximatedValue) ? (value - approximatedValue) : 0;
	}
	
	private double getValueWithDecimalPrecision(double value) {
		if(this.decimalPrecision <= 0) {
			return value;
		}
		
		value = value * Math.pow(10, this.decimalPrecision);
		
		if(this.approximation == ValueApproximation.CEIL) {
			value = Math.ceil(value);
		}
		else if(this.approximation == ValueApproximation.FLOOR) {
			value = Math.floor(value);
		}
		else {
			value = Math.round(value);
		}
			
		value = value / Math.pow(10, this.decimalPrecision);
		return value;
	}

	
}
