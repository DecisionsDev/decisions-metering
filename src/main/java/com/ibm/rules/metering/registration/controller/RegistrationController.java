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

package com.ibm.rules.metering.registration.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.rules.metering.Response;
import com.ibm.rules.metering.registration.repository.RegistrationEntity;
import com.ibm.rules.metering.registration.repository.RegistrationService;
import com.ibm.rules.metering.util.Environment;
import com.ibm.rules.metering.util.Messages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.rules.metering.GenericResponse;
import com.ibm.rules.metering.registration.RegistrationRequest;

@RestController("Registration")
@RequestMapping(value = "/api/startup", produces = "application/json")
public class RegistrationController {
	private final static Logger LOGGER = Environment.getLogger();
	
    @Autowired
    RegistrationService registrationService;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<GenericResponse> createInstance(@RequestBody RegistrationRequest registration) {
    	if(registration == null) {
    		LOGGER.warn(Environment.getMessage(Messages.INVALID_REGISTRATION_REQUEST_RECEIVED));
			return new ResponseEntity<GenericResponse>(Response.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
    	}
    	
    	String instanceIdentifier = registration.getInstanceIdentifier();
    	if(LOGGER.isTraceEnabled()) {
	    	try {
				String request = Environment.serializeObjectToJson(registration);
				LOGGER.trace(Environment.getMessage(Messages.REGISTRATION_REQUEST_RECEIVED, request));
			}
			catch (JsonProcessingException e) {
				LOGGER.trace(Environment.getMessage(Messages.REGISTRATION_REQUEST_RECEIVED_NO_INFO, e));
			}
    	}
    	
    	LOGGER.info(Environment.getMessage(Messages.REGISTRATION_REQUEST_RECEIVED_FROM_INSTANCE, instanceIdentifier)); 
    	
        try {
        	RegistrationEntity registrationEntity = RegistrationEntity.fromRegistrationRequest(registration);

        	boolean update = registrationService.exists(instanceIdentifier);
        	if(LOGGER.isTraceEnabled()) {
	        	if(update){
	        		LOGGER.trace(Environment.getMessage(Messages.REGISTRATION_REQUEST_UPDATE_RECORD, instanceIdentifier));
	        	}
	        	else {
	        		LOGGER.trace(Environment.getMessage(Messages.REGISTRATION_REQUEST_CREATE_RECORD, instanceIdentifier));
	        	}
        	}
        	
        	registrationService.saveOrUpdate(registrationEntity);
        	
        	if(update) {
        		LOGGER.info(Environment.getMessage(Messages.REGISTRATION_REQUEST_RECORD_UPDATED, instanceIdentifier));
        	}
        	else {
        		LOGGER.info(Environment.getMessage(Messages.REGISTRATION_REQUEST_RECORD_CREATED, instanceIdentifier));
        	}
        	
		} catch (Exception e) {
			LOGGER.error(Environment.getMessage(Messages.REGISTRATION_REQUEST_PROCESSING_ERROR), e);
			return new ResponseEntity<GenericResponse>(Response.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
        
        return new ResponseEntity<GenericResponse>(Response.CREATED_SUCCESSFULLY, HttpStatus.CREATED);
    }
}
