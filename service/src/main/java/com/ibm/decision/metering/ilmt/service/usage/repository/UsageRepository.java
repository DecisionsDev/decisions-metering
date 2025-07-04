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

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface UsageRepository extends CrudRepository<UsageEntity, String> {
	
	@Query("SELECT u FROM UsageEntity u WHERE u.id = ?1")
    List<UsageEntity> findByInstanceId(String instanceId);
	
	@Query("SELECT m FROM UsageEntity m WHERE m.instanceId = ?1 AND m.report IS null")
    List<UsageEntity> findByInstanceIdWhereNoReport(String instanceId);
	
	@Query("SELECT m FROM UsageEntity m WHERE m.report IS null")
    List<UsageEntity> finAllUsagesNotReported();
	
	@Query("SELECT m FROM UsageEntity m WHERE m.report IS NOT null")
    List<UsageEntity> finAllUsagesWithReportId();
	
	@Query("SELECT m FROM UsageEntity m WHERE m.report = ?1")
    List<UsageEntity> findByInstanceByReport(String report);
}
