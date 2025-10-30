package com.scotiabank.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class DriverUtils {
    protected static WebDriver driver;
    private static Path chromeTmpProfile; 

    public DriverUtils() { driver = getDriver(); }

    public void resetDriver() {
        if (driver != null) {
            try { driver.quit(); } catch (Exception ignored) {}
            driver = null;
        }
        
        if (chromeTmpProfile != null) {
            try {
                Files.walk(chromeTmpProfile)
                     .sorted((a,b) -> b.compareTo(a)) // delete children first
                     .forEach(p -> { try { Files.deleteIfExists(p); } catch (Exception ignored) {} });
            } catch (Exception ignored) {}
            chromeTmpProfile = null;
        }
    }

    public WebDriver getDriver() {
        if (driver == null) createDriver();
        return driver;
    }

    private void createDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions opts = new ChromeOptions();

        opts.addArguments(
                "--headless=new",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1920,1080"
        );

        
        try {
            chromeTmpProfile = Files.createTempDirectory("chrome-profile-");
            opts.addArguments("--user-data-dir=" + chromeTmpProfile.toAbsolutePath());
            opts.addArguments("--profile-directory=Default");
        } catch (Exception ignored) {}

        
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", "/tmp");
        opts.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(opts);
        
    }
}
