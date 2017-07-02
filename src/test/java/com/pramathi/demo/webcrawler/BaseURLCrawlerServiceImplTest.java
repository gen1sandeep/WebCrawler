/**
 * 
 */
package com.pramathi.demo.webcrawler;

import java.net.MalformedURLException;

import com.pramathi.demo.webcrawler.service.BaseURLCrawlerServiceImpl;
import com.pramathi.demo.webcrawler.service.CrawlerSerice;

import junit.framework.TestCase;
import org.junit.Test;
/**
 * @author Sandeep_Alla
 *
 */
public class BaseURLCrawlerServiceImplTest extends TestCase {

	/**
	 * Test method for
	 * {@link com.pramathi.demo.webcrawler.service.BaseURLCrawlerServiceImpl#process(java.lang.String, java.lang.String)}
	 * .
	 */
	@ Test(expected=MalformedURLException.class)
	public final void testProcess() {
		CrawlerSerice service = new BaseURLCrawlerServiceImpl();

		try {
			service.process("abc", "2016");
		} catch (MalformedURLException e) {
			
		}

	}

}
