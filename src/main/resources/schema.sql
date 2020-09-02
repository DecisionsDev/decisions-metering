
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
CREATE TABLE IF NOT EXISTS REGISTRATION(ID VARCHAR PRIMARY KEY, LAST_UPDATE BIGINT, DEFINITION VARCHAR);
CREATE TABLE IF NOT EXISTS USAGE(ID VARCHAR PRIMARY KEY, INSTANCE_ID VARCHAR, DEFINITION VARCHAR, REPORT VARCHAR);
CREATE TABLE IF NOT EXISTS REPORT(ID VARCHAR PRIMARY KEY, CREATION_DATE BIGINT, COMPLETION_DATE BIGINT, DEFINITION VARCHAR, STATUS INT);
CREATE TABLE IF NOT EXISTS METRIC(ID VARCHAR PRIMARY KEY, PERSISTENT_ID VARCHAR, SOFTWARE_PATH VARCHAR, METRIC_NAME VARCHAR, REMAINDER DOUBLE, REMAINDER_STATUS INT, LAST_UPDATE BIGINT);
