package com.scotiabank.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.*;
import com.scotiabank.utils.BrowserUtils;
import com.scotiabank.utils.ExcelConfig;
import java.time.Duration;


public class TFSApageclass extends BrowserUtils {

	// ===== Elements inside main calculator iframe =====
	@FindBy(css = "div.years-input input#termMain")
	private WebElement termInput;

	@FindBy(xpath = "(//span[@class='custom-radio-button'])[2]")
	private WebElement investPreferenceRadio;

	@FindBy(xpath = "//input[@id='lumpSumMain']")
	private WebElement startingAmountInput;

	@FindBy(xpath = "//input[@id='investmentMain']")
	private WebElement periodicInvestmentInput;

	@FindBy(xpath = "(//span[@class='custom-radio-button'])[7]")
	private WebElement monthlyFrequencyRadio;

	@FindBy(xpath = "//select[@id='provinceMain']")
	private WebElement provinceDropdown;

	@FindBy(xpath = "//input[@id='incomeMain']")
	private WebElement annualIncomeInput;

	@FindBy(xpath = "//a[@id='calculate']")
	private WebElement viewResultsButton;

	// After clicking "View Results", a results iframe/bar graph appears
	@FindBy(xpath = "//iframe[contains(@title,'Results') or contains(@src,'results')]")
	private WebElement resultsIframe;

	@FindBy(xpath = "//div[contains(@class,'result') or contains(@class,'chart') or contains(text(),'Results')]")
	private WebElement resultsSection;

	
	@FindBy(xpath = "//canvas[@id='tfsaGraph']")
	 private WebElement tfsaGraph;
	
	@FindBy(xpath = "//span[text()='View Video']")
	private WebElement viewVideo;

	// ===== Navigate =====
	public void navigateToTFSA() {
		String tfsaUrl = ExcelConfig.get("tfsaUrl");
		if (tfsaUrl == null || tfsaUrl.isEmpty()) {
			throw new RuntimeException(
					"TFSA URL not found in ExcelConfig. Please add 'tfsaUrl' in Scotibank_Data.xlsx");
		}
		driverGet(tfsaUrl);
		maximizeWindow();

		// === Handle Cookie Banner (OneTrust) ===
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement cookieBanner = wait
					.until(ExpectedConditions.presenceOfElementLocated(By.id("onetrust-banner-sdk")));
			WebElement acceptBtn = cookieBanner.findElement(By.id("onetrust-accept-btn-handler"));
			if (acceptBtn.isDisplayed()) {
				acceptBtn.click();
				Thread.sleep(1500);
			}
		} catch (Exception e) {
			System.out.println("No cookie banner found or already closed.");
		}

		// --- Switch to main calculator iframe ---
		switchToTFSAIframe();
	}

	private void switchToTFSAIframe() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement iframeElement = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//iframe[@title='Calculator tool']")));
		driver.switchTo().frame(iframeElement);
	}

	// ===== Actions =====
	public void enterTerm(String years) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement term = wait.until(ExpectedConditions.visibilityOf(termInput));
		term.click();
		term.sendKeys(Keys.CONTROL + "a");
		term.sendKeys(Keys.DELETE);
		for (char c : years.toCharArray()) {
			term.sendKeys(Character.toString(c));
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	public void selectInvestmentPreference() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(investPreferenceRadio));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", radio);
		js.executeScript("arguments[0].click();", radio);
	}

	public void enterStartingAmount(String amount) {
		startingAmountInput.click();
		startingAmountInput.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
		startingAmountInput.sendKeys(amount);
	}

	public void enterPeriodicInvestment(String amount) {
		periodicInvestmentInput.click();
		periodicInvestmentInput.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
		periodicInvestmentInput.sendKeys(amount);
	}

	public void selectFrequency(String freq) {
		if (freq.equalsIgnoreCase("Monthly")) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement monthly = wait.until(ExpectedConditions.elementToBeClickable(monthlyFrequencyRadio));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", monthly);
		}
	}

	public void selectProvince(String provinceCode) {
		Select province = new Select(provinceDropdown);
		province.selectByValue(provinceCode);
	}

	public void enterAnnualIncome(String income) {
		annualIncomeInput.click();
		annualIncomeInput.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
		annualIncomeInput.sendKeys(income);
	}

	// ===== Enhanced "View Results" Click =====
	public void clickViewResults() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            // Ensure the button is visible
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", viewResultsButton);
            Thread.sleep(500);

            // Click using JS to avoid intercepts
            js.executeScript("arguments[0].click();", viewResultsButton);
            System.out.println("🟢 Clicked on 'View Results' button successfully.");

            // Wait for results animation/render
            Thread.sleep(1500);

            // Switch out of iframe to look for Back to Top button
            driver.switchTo().defaultContent();

            // Wait for Back to Top button or results iframe
            try {
                wait.until(ExpectedConditions.or(
                        ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.bns--back-2-top.show")),
                        ExpectedConditions.presenceOfElementLocated(By.xpath("//iframe[contains(@title,'Results') or contains(@src,'results')]"))
                ));
                System.out.println("🟣 'Back to Top' button or results iframe detected.");
            } catch (TimeoutException te) {
                System.out.println("⚠️ 'Back to Top' button or results iframe not found in time.");
            }

            // Scroll slightly to trigger load
            js.executeScript("window.scrollBy(0, 400);");
            Thread.sleep(700);
            js.executeScript("window.scrollBy(0, -300);");
            Thread.sleep(700);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to click View Results or wait for result load", e);
        }
    }


	public boolean verifyResultsDisplayed() {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

	        // Wait for TFSA graph to load
	   //     wait.until(ExpectedConditions.visibilityOf(tfsaGraph));
	      //  System.out.println("📊 TFSA Graph is visible.");
	        // Wait for TFSA Graph to load
	        wait.until(ExpectedConditions.visibilityOf(tfsaGraph));
	        System.out.println("📊 TFSA Graph is visible.");
	        // Wait for "View Video" to appear
	        wait.until(ExpectedConditions.visibilityOf(viewVideo));
	        System.out.println("🎥 View Video section is visible.");


	        return viewVideo.isDisplayed() && tfsaGraph.isDisplayed();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
	
	