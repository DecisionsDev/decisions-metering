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
package com.ibm.rules.metering.metric.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;	

@Component
public interface MetricRepository extends CrudRepository<MetricEntity, String> {
	@Query("SELECT u FROM MetricEntity u WHERE u.persistentId = ?1 AND u.softwarePath = ?2 AND u.metricName = ?3")
    List<MetricEntity> findByInstanceIdAndMetric(String persistentId, String softwarePath, String metricName);
	
	@Query("SELECT u FROM MetricEntity u WHERE u.remainderStatus = ?1")
    List<MetricEntity> findByRemainderStatus(int remainderStatus);
}
