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
package com.ibm.decision.metering.ilmt.service.usage.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsageService {
	@Autowired
    UsageRepository usageRepository;

    public List<UsageEntity> getAllUsages() {
        List<UsageEntity> usages = new ArrayList<UsageEntity>();
        usageRepository.findAll().forEach(usage -> usages.add(usage));
        return usages;
    }
    
    public List<UsageEntity> getAllUsagesNotReported() {
        List<UsageEntity> usages = new ArrayList<UsageEntity>();
        usageRepository.finAllUsagesNotReported().forEach(usage -> usages.add(usage));
        return usages;
    }
    
    public List<UsageEntity> getAllUsagesWithReportId() {
        List<UsageEntity> usages = new ArrayList<UsageEntity>();
        usageRepository.finAllUsagesWithReportId().forEach(usage -> usages.add(usage));
        return usages;
    }
    
    public List<UsageEntity> getUsagesByInstanceId(String instanceId) {
        return usageRepository.findByInstanceId(instanceId);
    }

    public UsageEntity getUsageById(String id) {
        return usageRepository.findById(id).get();
    }

    public void saveOrUpdate(UsageEntity usage) {
    	usageRepository.save(usage);
    }

    public void delete(String id) {
    	usageRepository.deleteById(id);
    }
    
    public void deleteAll() {
    	usageRepository.deleteAll();
    }
    
    
}
