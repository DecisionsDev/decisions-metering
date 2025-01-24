/*
 *
 *   Copyright IBM Corp. 2025
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

public class Messages {
	private static final String ERROR = "E";
	private static final String INFO = "I";

	private static String infoToString(int n) {
		String module = "GBRCE";
		if (n < 10) {
			return module + "000" + n + INFO;
		} else if (n < 100) {
			return module + "00" + n +INFO;			
		} else if (n < 1000) {
			return module + "0" + n + INFO;
		} else {
			return module + n + INFO;
		}
	}

	private static String errorToString(int n) {
		String module = "GBRCE";
		if (n < 10) {
			return module + "000" + n + ERROR;
		} else if (n < 100) {
			return module + "00" + n + ERROR;			
		} else if (n < 1000) {
			return module + "0" + n + ERROR;
		} else {
			return module + n + ERROR;
		}
	}

	//
	public static final String ERROR_REQUEST_NULL_PRODUCT = errorToString(1004);
	
	public static final String ERROR_NULL_TYPE_IN_PARSED_ERROR = errorToString(1007);
	
	public static final String ERROR_NULL_MESSAGE_IN_PARSED_ERROR = errorToString(1008);
	
	public static final String ITEM_SEPARATOR = infoToString(1015);
}
