package com.pramathi.demo.webcrawler;

import static org.junit.Assert.*;

import javax.swing.text.html.parser.TagElement;

import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.pramathi.demo.webcrawler.service.persistence.FileSystemPersistenceServiceImpl;

public class FileSystemPersistenceServiceImplTest {

	

	@BeforeClass
	public static void setup() {
		FileSystemPersistenceServiceImpl testService = new FileSystemPersistenceServiceImpl();
		testService.saveMailDetails("sample text 1", "sample1");
		testService.saveMailDetails("sample text 2", "sample2");
	}

	
	@Test
	public final void testGetSavedMailCountforSuccess() {		
		assertTrue(FileSystemPersistenceServiceImpl.getSavedMailCount() == 2);
	}

	@Test
	public final void testGetSavedMailCountforFailure() {		
		assertFalse(FileSystemPersistenceServiceImpl.getSavedMailCount() > 2);
		assertFalse(FileSystemPersistenceServiceImpl.getSavedMailCount() < 2);
	}
}
