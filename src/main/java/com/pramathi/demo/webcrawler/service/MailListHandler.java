package com.pramathi.demo.webcrawler.service;

import static com.pramathi.demo.webcrawler.util.ConfigUtil.getConfigValue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.pramathi.demo.webcrawler.service.persistence.FileSystemPersistenceServiceImpl;
import com.pramathi.demo.webcrawler.util.JsoupUtil;

public class MailListHandler implements Runnable {

	private Element pageLink;
	private static final Logger logger = LogManager.getLogger(PaginationHandler.class);

	public MailListHandler(Element link) {
		this.pageLink = link;
	}

	@Override
	public void run() {
		try {
			Document mailListDoc = JsoupUtil.createDocumentFromURL(pageLink.attr("abs:href"));
			if (mailListDoc != null) {
				Elements mailList = mailListDoc.select("table#msglist").select("td.subject").select("a[href]");

				ExecutorService executor = Executors
						.newFixedThreadPool(Integer.parseInt(getConfigValue("MAX_THREADS_PER_PAGE")));

				for (Element mailLink : mailList) {
					executor.execute(() -> {
						Document mailDocument = JsoupUtil.createDocumentFromURL(mailLink.attr("abs:href"));
						if (mailDocument != null) {
							StringBuilder mailContent = new StringBuilder();
							Elements mail = mailDocument.select("table#msgview");
							String mailTimestamp = mail.select("tr.date").select("td.right").text();
							mailContent.append("From: ").append(mail.select("tr.from").select("td.right").text())
									.append("\n Subject: ").append(mail.select("tr.Subject").select("td.right").text())
									.append("\n Date: ").append(mailTimestamp).append("\n contents: ")
									.append(mail.select("tr.contents").select("pre").text());
							if (mailTimestamp.contains(",")) {
								mailTimestamp = mailTimestamp.replace(':', '_').split(",")[1].trim().replace(' ', '_');
							}
							new FileSystemPersistenceServiceImpl().saveMailDetails(mailContent.toString(), mailTimestamp);
						}
					});

				}
				executor.shutdown();

				executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
			}
		} catch (InterruptedException e) {
			logger.error("InterruptedException exception noticed while extracting mail List", e);
		} catch (Exception e) {
			// exceptions in runnable instance will be lost. Hence explicitly
			// catch all exceptions
			// and log them.
			logger.error("Error while processing ::" + pageLink.attr("abs:href"), e);

		}
	}
}
