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
package com.ibm.rules.metering.application.model;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class LicenseFile {
	File originatingFile;
	SoftwareIdentity softwareIdentity;
	List<Metric> metrics;
	
	public File getOriginatingFile() {
		return originatingFile;
	}

	public void setOriginatingFile(File originatingFile) {
		this.originatingFile = originatingFile;
	}

	public SoftwareIdentity getSoftwareIdentity() {
		return softwareIdentity;
	}

	public void setSoftwareIdentity(SoftwareIdentity softwareIdentity) {
		this.softwareIdentity = softwareIdentity;
	}

	public List<Metric> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<Metric> metrics) {
		this.metrics = metrics;
	}

	private LicenseFile() {
	}
	
	public static LicenseFile read(File f) throws Exception {
		LicenseFile instance = new LicenseFile();
		instance.originatingFile = f;
		
		// read and fix XML content
		String fileContent = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())));
		fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<license>"+fileContent+"</license>";
		
		// instanciate XML parser and parse document
		XmlMapper xmlMapper = new XmlMapper();
		JsonNode root = xmlMapper.readTree(fileContent);

		// retrieving software identity
		if(root.has(SoftwareIdentity.TAG_NAME)) {
			instance.softwareIdentity = xmlMapper.readValue(root.get(SoftwareIdentity.TAG_NAME).traverse(), SoftwareIdentity.class);
		}
		
		// manually finding Metric tags because jackson's xmlMapper does not find all
		List<Metric> metrics = new ArrayList<Metric>();
		String document = fileContent;
		String startPattern = "<Metric";
		String endPattern = "</Metric>"; 
		while(document.contains(endPattern)) {
			int startIndex = document.indexOf(startPattern);
			int endIndex = document.indexOf(endPattern) + endPattern.length();
			String fragment = document.substring(startIndex, endIndex);
			document = document.substring(endIndex);
			metrics.add(xmlMapper.readValue(xmlMapper.readTree(fragment).traverse(), Metric.class));
		}

		instance.metrics = metrics;
		
		return instance;
	}
	
	@Override
	public boolean equals(Object obj) {
		return originatingFile.equals(((LicenseFile)obj).originatingFile);
	}
	
	@Override
	public int hashCode() {
		return originatingFile.hashCode();
	}
}