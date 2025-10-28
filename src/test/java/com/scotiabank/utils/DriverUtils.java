package com.scotiabank.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverUtils {
	protected static WebDriver driver;

	public DriverUtils() {
		driver = getDriver();
	}

	public void resetDriver() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

	public WebDriver getDriver() {
		if (driver == null) {
			createDriver();
		}
		return driver;
	}

	private void createDriver() {
		String driverName = ExcelConfig.get("config", "browser");
		System.out.println(driverName);

		String browser = ExcelConfig.get("config", "browser").toLowerCase();

		switch (browser) {
		case "chrome":
		case "google":
		case "google chrome":
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;

		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			break;

		case "edge":
		case "msedge":
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;

		default:
		}
		driver.manage().window().maximize();
	}
}