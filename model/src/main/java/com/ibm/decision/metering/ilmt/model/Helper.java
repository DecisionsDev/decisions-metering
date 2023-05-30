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

package com.ibm.decision.metering.ilmt.model;

public class Helper {

	private static final String DOUBLE_QUOTE = "\""; // No_i18n
	
	private static final String EMPTY_STRING = ""; // No_i18n

	static public String appendString(String value) {
		String separator = value == null ? EMPTY_STRING : DOUBLE_QUOTE;
		StringBuilder tmp = new StringBuilder(separator);
		return tmp.append(value).append(separator).toString();
	}

}
