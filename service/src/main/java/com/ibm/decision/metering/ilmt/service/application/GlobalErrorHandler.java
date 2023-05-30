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
package com.ibm.decision.metering.ilmt.service.application;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import com.ibm.decision.metering.ilmt.service.util.Environment;
import com.ibm.decision.metering.ilmt.service.util.Messages;

public class GlobalErrorHandler {
	private final static Logger LOGGER = Environment.getLogger();
	
	public static String handleError(HttpServletRequest request, Exception exception) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if(exception == null) {
			exception = (Exception) request.getAttribute("javax.servlet.error.exception");;
		}
		
		String accessedURL = request.getRequestURL().toString();
		if(request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI) != null) {
			accessedURL = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
		}
    	
    	if(statusCode != null) {
    		LOGGER.error(Environment.getMessage(Messages.ERROR_URL_ACCESS_WITH_HTTP_CODE, accessedURL, statusCode), exception);  
    	}
    	else {
    		LOGGER.error(Environment.getMessage(Messages.ERROR_URL_ACCESS, accessedURL), exception);
    	}
    	
    	return Environment.getMessage(Messages.GENERIC_ERROR);
	}
}
