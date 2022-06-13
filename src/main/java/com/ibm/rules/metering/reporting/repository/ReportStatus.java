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
package com.ibm.rules.metering.reporting.repository;

public class ReportStatus {
	public final static int CREATED = 0;
	public final static int IN_PROCESS = 1;
	public final static int SUBMITTED = 2;
	public final static int NOT_SUBMITTED = 3;
	
	private ReportStatus() {
	}
	
	public static boolean isFinal(int status){
		return (status == SUBMITTED || status == NOT_SUBMITTED);
	}
}