package com.pramathi.demo.webcrawler;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pramathi.demo.webcrawler.service.BaseURLCrawlerServiceImpl;
import com.pramathi.demo.webcrawler.service.CrawlerSerice;
import com.pramathi.demo.webcrawler.service.persistence.FileSystemPersistenceServiceImpl;

/**
 * Entry point for the crawler application. Takes target URL and year as command
 * line inputs. e.g. 
 * java com.pramathi.demo.webcrawler.MainApp http://mail-archives.apache.org/mod_mbox/maven-users 2015
 * 
 * @author Sandeep_Alla
 */
public class MainApp {

	static long startTime;
	private static final Logger logger = LogManager.getLogger(MainApp.class);
	public static void main(String[] args) {

		startTime = System.nanoTime();

		if (args == null || args.length != 2) {
			System.err.println("Invalid input!!! " + "\nExpected input::"
					+ "\n \t java com.pramathi.demo.webcrawler.MainApp <URL> <Year>");
		} else {
			CrawlerSerice service = new BaseURLCrawlerServiceImpl();
			try {
				service.process(args[0], args[1]);
			} catch (IOException e) {
				logger.error("IO Exception noticed..",e);
			}

			System.out.println("\nTotal Processing time    :: " + getProcessingTime());
			// count retrived in dirty and quick manner. Can be retrieved in better way.
			System.out.println("\nTotal mails downloaded   :: " + FileSystemPersistenceServiceImpl.getSavedMailCount());
			
		}
	}

	/**
	 * return the process time as formated string
	 * @return formateedProcessingTime
	 */
	private static String getProcessingTime() {
		BigDecimal processingTime = new BigDecimal(System.nanoTime() - startTime);
		BigDecimal divisor = new BigDecimal(1000000000);
		processingTime = processingTime.divide(divisor).setScale(2, RoundingMode.HALF_DOWN);
		int seconds = (processingTime.intValue()) % 60;
		int totalMinutes = processingTime.intValue() / 60;
		int minutes = totalMinutes % 60;
		int hours = totalMinutes / 60;

		if (hours != 0)
			return hours + " hours " + minutes + " minutes " + seconds + " seconds";
		else if (minutes != 0)
			return minutes + " minutes " + seconds + " seconds";
		else
			return processingTime + " seconds";
	}
}
