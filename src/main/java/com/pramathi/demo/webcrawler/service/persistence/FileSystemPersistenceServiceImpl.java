package com.pramathi.demo.webcrawler.service.persistence;

import static com.pramathi.demo.webcrawler.util.ConfigUtil.getConfigValue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileSystemPersistenceServiceImpl implements MailSaveService {

	private static final Logger logger = LogManager.getLogger(FileSystemPersistenceServiceImpl.class);
	private static String directoryPath = null;
	// to be changed
	public static AtomicInteger mailCounter = new AtomicInteger(0);
	static {

		// ideally static block would be executed only once per class.
		// synchronized block to be removed,if redundent -- check later
		synchronized (FileSystemPersistenceServiceImpl.class) {
			File file = new File(getConfigValue("DOWNLOAD_DIR_PATH")+File.separator
					+ new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()));

			if (!file.exists() && file.mkdir()) {
				
				System.out.println("\nMails Saved to path      :: " + file.getAbsolutePath());
				directoryPath = file.getAbsolutePath() + "/";

				logger.debug("Download directory path::" + directoryPath);
				
			} else {				
				logger.error("LOG_FILES_PATH is invalid.  Current Value:  " + getConfigValue("REPORT_PATH"), new IOException());

			}
		}

	}

	@Override
	/*
	 * Will save mail content to file system (non-Javadoc)
	 * 
	 * @see com.pramathi.demo.webcrawler.service.persistence.MailSaveService#
	 * saveMailDetails(java.lang.String)
	 */
	public void saveMailDetails(String fileContent, String fileName) {

		try (FileWriter fileWriter = new FileWriter(directoryPath + File.separator + fileName + ".txt");
				BufferedWriter writer = new BufferedWriter(fileWriter);) {
			writer.write(fileContent);
			logger.debug("written file" + fileName);
			mailCounter.getAndIncrement();
		} catch (IOException e) {
			logger.error("Error while saving mail ::" + fileName, e);
		}
	}
	
	public static int getSavedMailCount(){
		return mailCounter.get();
	}

}
