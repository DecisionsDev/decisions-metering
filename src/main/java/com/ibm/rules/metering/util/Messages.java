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
package com.ibm.rules.metering.util;

public class Messages {
	// general messages
	public final static String PRODUCT_NAME = "product_name";
	public final static String ABBREVIATED_PRODUCT_NAME = "abbreviated_product_name";
	public final static String APPLICATION_STARTING = "application_starting";
	public final static String APPLICATION_READY = "application_ready";
	public final static String GENERIC_ERROR = "generic_error_message";
	public final static String CONFIGURATION_ERROR = "configuration_error_message";
	public final static String INIT_PROPERTIES_ERROR = "initialization_properties_error_message";
	public final static String ERROR_URL_ACCESS = "error_on_URL";
	public final static String ERROR_URL_ACCESS_WITH_HTTP_CODE = "error_on_URL_with_HTTP_code";
	
	// registration-related messages
	public final static String INVALID_REGISTRATION_REQUEST_RECEIVED = "invalid_registration_request_received";
	public final static String REGISTRATION_REQUEST_RECEIVED = "registration_request_received";
	public final static String REGISTRATION_REQUEST_RECEIVED_NO_INFO = "registration_request_received_no_info";
	public final static String REGISTRATION_REQUEST_RECEIVED_FROM_INSTANCE = "registration_request_received_from_instance";
	public final static String REGISTRATION_REQUEST_UPDATE_RECORD = "registration_request_update_record";
	public final static String REGISTRATION_REQUEST_CREATE_RECORD = "registration_request_create_record";
	public final static String REGISTRATION_REQUEST_RECORD_UPDATED = "registration_request_record_updated";
	public final static String REGISTRATION_REQUEST_RECORD_CREATED = "registration_request_record_created";
	public final static String REGISTRATION_REQUEST_PROCESSING_ERROR = "registration_processing_error";
	
	// usage-related messages
	public final static String INVALID_USAGE_REPORT_REQUEST_RECEIVED = "invalid_usage_request_received";
	public final static String EMPTY_USAGE_REPORT_REQUEST_RECEIVED = "empty_usage_request_received";
	public final static String USAGE_REPORT_REQUEST_RECEIVED = "usage_request_received";
	public final static String USAGE_REPORT_REQUEST_RECEIVED_NO_INFO = "usage_request_received_no_info";
	public final static String USAGE_REPORT_REQUEST_SAVED = "usage_request_saved";
	public final static String USAGE_REPORT_REQUEST_SAVE_ERROR = "usage_request_save_error";
	public final static String USAGE_REPORT_REQUEST_IRRELEVANT = "usage_request_irrelevant";
	public final static String USAGE_REPORT_REQUEST_PROCESSING_ERROR = "usage_request_processing_error";
	
	// processing-related messages
	public final static String BACKUP_START = "backup_starting";
	public final static String BACKUP_END = "backup_ending";
	public final static String REPORTING_START = "starting_reporting";
	public final static String REPORTING_END = "ending_reporting";
	public final static String REPORTING_END_WITH_ID = "ending_reporting_with_id";
	public final static String IMPOSSIBLE_PROCESSING_INVALID_CONFIGURATION = "cannot_perform_processing_configuration_invalid";
	public final static String IMPOSSIBLE_PROCESSING_NO_REGISTRATION = "no_registration_impossible_processing";
	public final static String NO_USAGE_TO_PROCESS = "no_usage_to_process";
	public final static String USAGES_TO_PROCESS = "usages_to_process";
	public final static String USAGES_PROCESSING_SESSION_ID = "report_created_id"; 
	public final static String UNKNOWN_SOFTWARE_INSTANCE = "unknown_related_instance";
	public final static String INVALID_SOFTWARE_PATH = "invalid_software_path_for_usage";
	public final static String USAGES_USED_FOR_PROCESSING = "usages_used_for_report";
	public final static String USAGES_REPORT_SUCCESS = "ilmt_write_success";
	public final static String USAGES_REPORT_ERROR = "ilmt_write_failure";
	public final static String NO_AGGREGATION_TO_DO = "no_aggregation_to_do";
	public final static String NULL_METRIC_NOT_REPORTED = "null_metric_not_reported";
	public final static String REPORTING_METRIC = "reporting_metric";
	public final static String ILMT_WRITE_SUCCESS = "ilmt_write_ok";
	public final static String ILMT_WRITE_DETAILS = "ilmt_write_details";
	public final static String REPORT_PROCESSING_ERROR = "report_processing_error";
	public final static String PROCESSING_DELETE_USAGES_SUCCESS = "delete_processed_usages_success";
	public final static String PROCESSING_DELETE_USAGES_ERROR = "delete_processed_usages_failure";
	public final static String TOTAL_USAGES_RESULT = "usages_process_result";
	public final static String REMAINDER_FOUND = "usages_process_remainder_found";
	public final static String NO_REMAINDER_FOUND = "usages_process_no_remainder_found";
	public final static String REMAINDER_TO_BE_PROCESSED = "usages_process_remainder_to_process";
	public final static String NO_REMAINDER_TO_BE_PROCESSED = "usages_process_no_remainder_to_process";
	public final static String CLEANUP_START = "starting_cleanup";
	public final static String CLEANUP_DELETED_RECORDS = "cleanup_deleted_records";
	public final static String CLEANUP_NO_DELETED_RECORDS =	"cleanup_no_deleted_records";
	public final static String CLEANUP_UNFLAGGED_RECORDS = "cleanup_unflagged_records";
	public final static String CLEANUP_NO_UNFLAGGED_RECORDS = "cleanup_no_unflagged_records";
	public final static String CLEANUP_REMAINDER_TO_PROCESS  = "cleanup_remainder_back_to_process";
	public final static String CLEANUP_NO_REMAINDER_TO_PROCESS  = "cleanup_no_remainder_back_to_process";
	public final static String CLEANUP_END = "ending_cleanup";
	public final static String CLEANUP_ERROR = "cleanup_error";
}
