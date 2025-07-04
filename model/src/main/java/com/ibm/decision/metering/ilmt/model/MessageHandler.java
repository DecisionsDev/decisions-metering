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

import java.text.MessageFormat;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageHandler {

	private final String resourceBundles = getClass().getPackage().getName()
			+ ".messages"; // No_i18n

	public String getMessage(String key, Object... parameters) {
		return getMessage(false, key, parameters);
	}

	private String getMessage(boolean includeMessageCode, String key,
			Object... parameters) {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(resourceBundles);
			String message = MessageFormat.format(bundle.getString(key), parameters);
			if(includeMessageCode) return key + ": " + message;
			return message;
			
	    } catch (MissingResourceException e) {
	    	return "";
	    }
	}

	public String getSevereMessage(String key, Object... parameters) {
		return getMessage(true, key, parameters);
	}
}
