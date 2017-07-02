package com.pramathi.demo.webcrawler.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Utility to read config parameters.
 *  File I/O is done only once during first time class loading.
 * @author Sandeep_Alla
 *
 */
public class ConfigUtil {

	static Map<String, String> configList = new HashMap<String, String>();
	static {
		try {
			File file = new File("./config.xml");
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.loadFromXML(fileInput);
			fileInput.close();

			Enumeration<Object> enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				configList.put(key, properties.getProperty(key));
			}
		} catch (FileNotFoundException e) {
			System.out.println("Ohh..oo something went wrong while reading Configurtion file");
			e.printStackTrace();
			// quitting program. normally should be a critical exception
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Ohh..oo something went wrong while reading Configurtion file");
			e.printStackTrace();
			// quitting program. normally should be a critical exception
		}
	}

	public static String getConfigValue(String ConfigName) {
		if (ConfigName != null && configList.containsKey(ConfigName)) {
			return configList.get(ConfigName);
		} else {
			System.out.println("grrrrrrr...I cannot return value of configuration, which does not exist!!!!");
			return null;
		}
	}
}
