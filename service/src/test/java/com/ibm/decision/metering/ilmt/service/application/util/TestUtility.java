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
package com.ibm.decision.metering.ilmt.service.application.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ibm.decision.metering.ilmt.service.application.model.LicenseFile;

@Component
@EnableConfigurationProperties
public class TestUtility {
	
	@Value("${server.port}")
	private int port;
	
	@Value("${com.ibm.decision.metering.ilmt.service.ILMToutputDirectory}")
	private String licenseFilesOutputDirectory;
	
	@Autowired
	RequestFactory requestFactory;
	
	
	public Response sendRegistrationRequest(String instanceIdentifier, String persistentId, String installDirectory) throws Exception {
		String request = requestFactory.createRegistrationRequest(instanceIdentifier, persistentId, installDirectory);
		return org.apache.http.client.fluent.Request
			.Post(getRegistrationURL())
			.bodyString(request, ContentType.APPLICATION_JSON)
			.execute();
	}
	
	public Response sendUsageRequest(String usageType, long startTime, long endTime, long decisions, String instanceIdentifier, String installDirectory) throws Exception {
		String request = requestFactory.createUsageRequest(usageType, startTime, endTime, decisions, instanceIdentifier, installDirectory);
		return org.apache.http.client.fluent.Request
			.Post(getUsageURL())
			.bodyString(request, ContentType.APPLICATION_JSON)
			.execute();
	}
	
	public boolean doesOutputLicenseFilesDirectoryExist() {
		if(licenseFilesOutputDirectory == null) return false;
		File f = new File(licenseFilesOutputDirectory);
		return (f != null && f.exists() && f.isDirectory());
	}
	
	public void deleteLicenseFilesDirectoryIfExists() throws Exception{
		if(! doesOutputLicenseFilesDirectoryExist()) return;
		
		 Files.walk(Paths.get(licenseFilesOutputDirectory))
		 .map(p -> p.toFile())
		 .forEach(f -> f.delete());
	}
	
	public int getLicenseFilesCount() throws Exception {
		if(! doesOutputLicenseFilesDirectoryExist()) return 0;
		
		File f = new File(licenseFilesOutputDirectory);
		return f.listFiles().length;
	}
	
	public List<LicenseFile> getLicenseFiles() throws Exception{
		if(! doesOutputLicenseFilesDirectoryExist()) throw new Exception("The license files directory '"+licenseFilesOutputDirectory+"' could not be found.");
		
		File f = new File(licenseFilesOutputDirectory);
		List<File> files = Arrays.asList(f.listFiles());
		List<LicenseFile> licenseFiles = new ArrayList<LicenseFile>();
		for(File file: files) {
			licenseFiles.add(LicenseFile.read(file));
		}
		
		return licenseFiles;
	}
	
	public List<LicenseFile> getAddedLicenseFiles(List<LicenseFile> licenseFilesBeforeProcessing, List<LicenseFile> licenseFilesAfterProcessing) throws Exception{
		List<LicenseFile> addedLicenseFiles = new ArrayList<LicenseFile>();
		
		licenseFilesAfterProcessing.stream().forEach(lf -> {
			if(! licenseFilesBeforeProcessing.contains(lf)) {
				addedLicenseFiles.add(lf);
			}
		});
		
		return addedLicenseFiles;
	}
	
	private String getRegistrationURL() {
		return getBaseURL()+"/api/startup";
	}
	
	private String getUsageURL() {
		return getBaseURL()+"/api/usage";
	}
	
	private String getBaseURL() {
		return "http://localhost:"+this.port;
	}
}
