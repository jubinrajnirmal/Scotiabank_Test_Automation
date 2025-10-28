package com.scotiabank.utils;


import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks extends BrowserUtils {

	@Before
	public void setUp() {
		getDriver();

		String baseUrl = ExcelConfig.get("baseUrl");

		if (baseUrl != null && !baseUrl.trim().isEmpty() && !"Invalid property key!!".equalsIgnoreCase(baseUrl)) {
			driver.get(baseUrl.trim());
		}
	}
	
	
	@After
	public void tearDown() {
		resetDriver();
	}
	
	
	
}
