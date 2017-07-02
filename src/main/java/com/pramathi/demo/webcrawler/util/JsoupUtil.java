package com.pramathi.demo.webcrawler.util;

import static com.pramathi.demo.webcrawler.util.ConfigUtil.getConfigValue;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.pramathi.demo.webcrawler.service.persistence.FileSystemPersistenceServiceImpl;
/**
 * Utility file to centralize redudant code
 * @author Sandeep_Alla
 *
 */
public class JsoupUtil {
	
	private static final Logger logger = LogManager.getLogger(JsoupUtil.class);
	/**
	 * connects to the target URL, retrives HTML content
	 * and initializes document instance. Uses the CONNECTION_TIME_OUT configuration pameter.
	 * @param URL
	 * @return
	 */
	public static Document createDocumentFromURL(String URL){
		Document doc = null;
		try {
			// connect to current URL and initialize doc
			logger.debug("Connecting to URL :: " +URL);
			doc = Jsoup.connect(URL)
						.timeout(Integer.parseInt(getConfigValue("CONNECTION_TIME_OUT")))
						.get();			
		} catch (IOException e) {
			logger.error("Error while connecting to [" + URL + "]. Refer stacktrace for detailed info",e);
			
		}
		return doc;
	}
}
