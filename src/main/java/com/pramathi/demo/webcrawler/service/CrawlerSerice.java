package com.pramathi.demo.webcrawler.service;

import java.io.IOException;
import java.net.MalformedURLException;

public interface CrawlerSerice {

	public void process(String url, String year) throws MalformedURLException;
}
