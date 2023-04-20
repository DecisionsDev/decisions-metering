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
package com.ibm.rules.metering.util;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.rules.metering.reporting.MetricConfiguration;

public class Environment {
	private final static String MESSAGE_BUNDLE = "metering_service_messages";
	private final static String DEFAULT_PROPERTIES_FILE = "/metering_service_default.properties";
	private final static String LOGGER_NAME = "com.ibm.rules.metering.service.ILMT";
	public static MetricConfiguration METRIC_CONFIGURATION = null;
	
	public static Logger getLogger() {
		return LoggerFactory.getLogger(LOGGER_NAME);
	}
	
	public static String getMessage(String messageKey, Object... messageParameters) {
		ResourceBundle resource;
		String message = null;
		
        try {
    		resource = ResourceBundle.getBundle(MESSAGE_BUNDLE);

            if (messageParameters == null) {
            	message = resource.getString(messageKey);
            } 
            else {
            	message = MessageFormat.format(resource.getString(messageKey), messageParameters);
            }

            return message;
        } catch (Throwable t) {
        	return messageKey;
        }
	}
	
	public static Properties getDefaultProperties() {
		try {
			Properties defaultProperties = new Properties();
			defaultProperties.load(Environment.class.getResourceAsStream(DEFAULT_PROPERTIES_FILE));
			return defaultProperties;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public static String getDefaultProperty(String propertyName) {
		try {
			Properties defaultProperties = new Properties();
			defaultProperties.load(Environment.class.getResourceAsStream(DEFAULT_PROPERTIES_FILE));
			return defaultProperties.getProperty(propertyName);
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public static <T> T deserializeObjectFromJson(String content, Class<T> objectType) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(content, objectType);
	}
	
	public static <T> T deserializeObjectFromJsonOrDefault(String content, Class<T> objectType, T defaultValue) {
		try {
			return deserializeObjectFromJson(content, objectType);
		}
		catch(Exception e) {
			return defaultValue;
		}
	}
	
	public static String serializeObjectToJson(Object objectToSerialize) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(objectToSerialize);
	}
	
	public static String serializeObjectToJsonOrDefault(Object objectToSerialize, String defaultValue) {
		try {
			return serializeObjectToJson(objectToSerialize);
		} 
		catch (Exception e) {
			return defaultValue;
		}
	}
}
