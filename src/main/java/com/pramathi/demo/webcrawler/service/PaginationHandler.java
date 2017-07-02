package com.pramathi.demo.webcrawler.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.pramathi.demo.webcrawler.MainApp;
import com.pramathi.demo.webcrawler.util.JsoupUtil;;

public class PaginationHandler implements Runnable {
	private static final Logger logger = LogManager.getLogger(PaginationHandler.class);
	private Element monthLink;

	public PaginationHandler(Element link) {
		this.monthLink = link;
	}

	@Override
	public void run() {
		try {
			// lambda expression equivalent to overrride run() of
			// Runnable interface
			Elements subPageLinks = new Elements();
			subPageLinks.add(monthLink);
			Document monthDocument = JsoupUtil.createDocumentFromURL(monthLink.attr("abs:href"));
			if (monthDocument != null) {
				/*
				 * parse the HTML data 
				 * 1. identify the <table> tags with id="msglist" 
				 * 2. extract <th> tage with style name ="pages" 
				 * 3. Get absolute url's of links with <a> tag elements containing text only numbers
				 */

				subPageLinks.addAll(
						monthDocument.select("table#msglist").select("th.pages").select("a[href]:matchesOwn(\\d+)"));

				// 3. iterate over all pagiation links for given month
				// concurrently
				ExecutorService subpageExecutor = Executors.newFixedThreadPool(subPageLinks.size());

				for (Element subpageLink : subPageLinks) {
					MailListHandler worker = new MailListHandler(subpageLink);
					subpageExecutor.execute(worker);
				}
				subpageExecutor.shutdown();

				try {
					subpageExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					logger.error("InterruptedException exception noticed while sub-pages links",e);
					
				}

			}
		} catch (Exception e) {
			// exceptions in runnable instance will be lost. Hence explicitly catch all exceptions
			// and log them.
			logger.error("Error while processing ::"+monthLink.attr("abs:href"),e);
			
		}
	}

}
