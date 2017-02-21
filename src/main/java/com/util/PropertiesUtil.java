package com.util;

import java.io.IOException;
import java.util.Properties;

import com.shared.FileService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

/**
 * @author Vinit Solanki
 *
 */
@Component
public class PropertiesUtil {

	public static Properties prop;

	private static FileService fileService;

	@Autowired
	public PropertiesUtil(FileService fileService) {
		super();
		this.fileService = fileService;
	}

	private static final Logger log = Logger.getLogger(PropertiesUtil.class);

	public static Properties setProprtyFile(String propertyFile) {
		if (prop == null)
			prop = new Properties();
		try {
			prop.load(fileService.getResource(ResourceUtils.CLASSPATH_URL_PREFIX, propertyFile, false).getInputStream());
		} catch (IOException e) {
			//log.error("Exception while set Property : " + propertyFile + "| Exception :" + e.printStackTrace());
			log.error("Exception while set Property : " + propertyFile + "| Exception :" + e.getMessage());
		}
		return prop;
	}

	public String getValue(String key) {

		return prop.getProperty(key);
	}

	public String getValue(String key, String defaultValue) {

		return prop.getProperty(key, defaultValue);
	}

}
