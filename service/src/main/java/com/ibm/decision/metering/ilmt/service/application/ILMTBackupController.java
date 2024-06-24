 /*
 *
 *   Copyright IBM Corp. 2024
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
package com.ibm.decision.metering.ilmt.service.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.decision.metering.ilmt.service.util.Environment;
import com.ibm.decision.metering.ilmt.service.util.Messages;

@Controller
public class ILMTBackupController {

	@Value("${com.ibm.decision.metering.ilmt.service.ILMToutputDirectory}")
	String ILMT_outputDirectory;

	private final static Logger LOGGER = Environment.getLogger();
	private final static String ILMT_FILE_EXTENSION=".*\\.slmtag";

	private int fileNumber = 0;

	@RequestMapping("/backup")
	@ResponseBody
	public void backup(HttpServletResponse response) {
		try{
		fileNumber = 0;

		if (this.ILMT_outputDirectory == null){
		  LOGGER.info(Environment.getMessage(Messages.BACKUP_START,"fallback : "+ this.ILMT_outputDirectory));
		  ILMT_outputDirectory="/config/storage/ILMT";
		} else {
		  LOGGER.info(Environment.getMessage(Messages.BACKUP_START,"com.ibm.decision.metering.ilmt.service.ILMToutputDirectory: "+ this.ILMT_outputDirectory));
		}

		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "filename=ILMTBackup_"+Long.toString(System.currentTimeMillis())+".zip");

		try(ZipOutputStream zip = new ZipOutputStream(response.getOutputStream())){
		   final File ILMT_folder = new File(this.ILMT_outputDirectory);
		   List<File> ILMT_files = new ArrayList<File>();
		   if (ILMT_folder.exists()){
	             search(ILMT_FILE_EXTENSION, ILMT_folder, ILMT_files);
		   }
		   for (File file : ILMT_files) {
		     addToZipFile(file, zip);
		     fileNumber++;
		   }
		zip.flush();
		LOGGER.info(Environment.getMessage(Messages.BACKUP_END, fileNumber)); 
               }catch (Exception e) {
		   LOGGER.error(Environment.getMessage(Messages.GENERIC_ERROR), e);}
		finally {
		   response.flushBuffer();
		}

		}catch(Exception e){
		 LOGGER.error(Environment.getMessage(Messages.GENERIC_ERROR), e);
		}
	}

	public static void addToZipFile(File file, ZipOutputStream zos) throws Exception {
		InputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(file.getName());
		try {
			zos.putNextEntry(zipEntry);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zos.write(bytes, 0, length);
			}
		}
		finally {
			zos.closeEntry();
			fis.close();
		}
	}

	public static void search(final String pattern, final File folder, List<File> files) {
	for (final File f : folder.listFiles()) {
	  if (f.isDirectory()) {
	     search(pattern, f, files);}

	  if (f.isFile()) {
	    if (f.getName().matches(pattern)) {
	      files.add(f);}
          }
        }
        }
}
