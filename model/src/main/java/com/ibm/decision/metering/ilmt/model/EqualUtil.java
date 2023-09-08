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

package com.ibm.decision.metering.ilmt.model;


public class EqualUtil {

	public static <T> boolean hasSameValue(T lhs, T rhs) {
		if (lhs == null) {
			return rhs == null;
		}
		return lhs.equals(rhs);
	}

	public static void assertNotNull(Object object, String key, Object... arguments)
			throws IllegalStateException {
		if (object == null) {
			String message = new MessageHandler().getMessage(key, arguments);
			throw new IllegalStateException(message);
		}
	}
}
