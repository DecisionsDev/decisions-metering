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
package com.ibm.rules.metering.registration.repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

	    @Autowired
	    RegistrationRepository registrationRepository;

	    public List<RegistrationEntity> getAllRegistrations() {
	        List<RegistrationEntity> registrations = new ArrayList<RegistrationEntity>();
	        registrationRepository.findAll().forEach(registration -> registrations.add(registration));
	        return registrations;
	    }

	    public RegistrationEntity getRegistrationById(String id) {
	        return registrationRepository.findById(id).get();
	    }
	    
	    public boolean exists(String id) {
	        return registrationRepository.findById(id).isPresent();
	    }

	    public void saveOrUpdate(RegistrationEntity registration) {
	    	registration.setLastUpdate(Instant.now().toEpochMilli());
	    	registrationRepository.save(registration);
	    }

	    public void delete(String id) {
	    	registrationRepository.deleteById(id);
	    }
}
