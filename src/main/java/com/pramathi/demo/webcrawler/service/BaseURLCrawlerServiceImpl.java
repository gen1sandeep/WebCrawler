package com.pramathi.demo.webcrawler.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.pramathi.demo.webcrawler.MainApp;
import com.pramathi.demo.webcrawler.util.JsoupUtil;

/**
 * Crawler service uses jsoup library to access and parse the HTML.
 * 
 * @author Sandeep_Alla
 */
public class BaseURLCrawlerServiceImpl implements CrawlerSerice{

	private final Logger logger = LogManager.getLogger(this);
	/**
	 * Accepts url and target year for which mails are to be downloaded
	 * 
	 * @param url
	 * @param year
	 * @throws MalformedURLException 
	 * @throws IOException
	 */
	public void process(String url, String year) throws MalformedURLException{

		if(!url.contains(".")){
			throw new MalformedURLException("Invalid URL.."+url);
		}
		
		
		// 1. extract links for each month based on target year
		Elements monthlyLinks = extractLinksForAvailableMonths(url, year);
		

		
		if (monthlyLinks == null) {
			return;
		}

		// 2. iterate over all months pages concurrently and create list sub-pagination links

		ExecutorService monthExecutor = Executors.newFixedThreadPool(monthlyLinks.size());
		for (Element monthLink : monthlyLinks) {
			PaginationHandler worker = new PaginationHandler(monthLink);
			monthExecutor.execute(worker);
		}

		monthExecutor.shutdown();
		try {
			monthExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.error("InterruptedException exception noticed while processing links for each month",e);

		}
	}

	/**
	 * util method to extract sub-url's for each month from source url.
	 * 
	 * @param url
	 * @param year
	 * @return
	 * @throws IOException
	 */
	private Elements extractLinksForAvailableMonths(String url, String year) {
		Elements monthWiseLinks;

		// notify progress on console
		System.out.println("\n\nconnecting to url :: " + url);

		Document doc = JsoupUtil.createDocumentFromURL(url);
		if (doc != null) {
			// parse the HTML data
			// 1. identify the <table> tags with class="year"
			// 2. Filter the tables containing text "Year $target_year$"
			// 3. Get absolute url's of links with <a> tag elements containing
			// text "Date"
			monthWiseLinks = doc.select("table.year:contains(Year " + year + ")").select("a[href]:contains(Date)");

			System.out.println("\nTarger Year  ::  [" + year + "],  Mails Available for  ::  [" + monthWiseLinks.size() + "] months");
			return monthWiseLinks;
		} else {
			return null;
		}
	}

}
