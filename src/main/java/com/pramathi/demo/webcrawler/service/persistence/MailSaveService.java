package com.pramathi.demo.webcrawler.service.persistence;

import java.io.IOException;

public interface MailSaveService {

	/**
	 * To be used when the content of mail is to be persisted, can be either in
	 * Disk (File System), Database etc.
	 * 
	 * @param mailContent
	 * @throws IOException
	 */
	public void saveMailDetails(String mailContent,String filename) throws IOException;
}
