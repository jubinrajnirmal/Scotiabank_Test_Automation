package com.scotiabank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.scotiabank.utils.BrowserUtils;
import com.scotiabank.utils.ExcelConfig;

public class CCRewardsCalculatorPage extends BrowserUtils {

	// Locators
	@FindBy(xpath = "//h1[contains(text(),'rewards calculator')]")
	private WebElement pageTitle;
	@FindBy(xpath = "//*[contains(text(),'Enter your monthly spend and calculate rewards')]//ancestor::div[contains(@data-id,'creditCardRewardsCalculator')]")
	private WebElement calcSection;
	@FindBy(xpath = "//*[contains(text(),'Detailed spend')]//ancestor::li")
	private WebElement detailedSpendTab;
	@FindBy(xpath = "//*[contains(text(),'Average spend')]//ancestor::li")
	private WebElement averageSpendTab;
	@FindBy(xpath = "//input[contains(@id,'groc-total')]")
	private WebElement groceriesInput;
	@FindBy(xpath = "//input[contains(@id,'bill-total')]")
	private WebElement billsInput;
	@FindBy(xpath = "//input[contains(@id,'enter-total')]")
	private WebElement entertainmentInput;
	@FindBy(xpath = "//input[contains(@id,'trans-total')]")
	private WebElement transportationInput;
	@FindBy(xpath = "//input[contains(@id,'home-total')]")
	private WebElement homeImprovementInput;
	@FindBy(xpath = "//input[contains(@id,'travel-total')]")
	private WebElement travelInput;
	@FindBy(xpath = "//input[contains(@id,'other-total')]")
	private WebElement otherPurchasesInput;
	@FindBy(xpath = "//a[contains(@href,'#/rewards-calc')]")
	private WebElement calcRewardsBtn;
	@FindBy(xpath = "//*[normalize-space()='Average spend']//ancestor::div[@role='tab']")
	private WebElement averageTabTrigger;
	@FindBy(xpath = "//*[normalize-space()='Detailed spend']//ancestor::div[@role='tab']")
	private WebElement detailedTabTrigger;
	@FindBy(xpath = "//input[contains(@inputmode,'decimal')]")
	private WebElement averageSpendInput;
	@FindBy(xpath = "//*[normalize-space()='Total monthly spend']/following::*[self::span or self::div][1]")
	private WebElement totalMonthlySpend;

	// Helpers
	public void navigateToCCRewardsCalculatorPage() {
		String url = driver.getCurrentUrl();
		driver.get(ExcelConfig.get("ccRewardCalcUrl"));
		waitForUrlChangeFrom(url);
		closeCookieBannerIfPresent();
	}

	public boolean isPageTitleVisible() {
		waitForVisibilityOf(pageTitle);
		return pageTitle.isDisplayed();
	}

	public boolean isCalculatorSectionVisible() {
		waitForVisibilityOf(calcSection);
		return calcSection.isDisplayed();
	}

	public boolean isDetailedSpendTabVisible() {
		waitForVisibilityOf(detailedSpendTab);
		return detailedSpendTab.isDisplayed();
	}

	public boolean isDetailedSpendTabEnabled() {
		waitForVisibilityOf(detailedSpendTab);
		return detailedSpendTab.isEnabled();
	}

	public boolean isAverageSpendTabVisible() {
		waitForVisibilityOf(averageSpendTab);
		return averageSpendTab.isDisplayed();
	}

	public boolean isAverageSpendTabEnabled() {
		waitForVisibilityOf(averageSpendTab);
		return averageSpendTab.isEnabled();
	}

	private boolean isTabActive(WebElement tab) {
		waitForVisibilityOf(tab);
		WebElement trigger = tab.findElement(By.xpath(".//div[@role='tab']"));
		String aria = trigger.getAttribute("aria-selected");
		String cls = trigger.getAttribute("class");
		boolean ariaActive = "true".equalsIgnoreCase(aria);
		boolean classActive = cls != null && cls.contains("Tab_link--active");
		return ariaActive || classActive;
	}

	public boolean isDetailedSpendTabActive() {
		return isTabActive(detailedSpendTab);
	}

	public boolean isAverageSpendTabActive() {
		return isTabActive(averageSpendTab);
	}

	public String currentHash() {
		Object h = ((JavascriptExecutor) driver).executeScript("return window.location.hash || '';");
		return h == null ? "" : h.toString();
	}

	public boolean isCategoryVisible(String categoryText) {
		waitForVisibilityOf(calcSection);
		By category = By.xpath("//h3[contains(text(),'" + categoryText + "')]");
		return calcSection.findElements(category).stream().anyMatch(WebElement::isDisplayed);
	}

	public boolean isCalculateButtonEnabled() {
		waitForVisibilityOf(calcRewardsBtn);
		return calcRewardsBtn.isEnabled();
	}

	public void clickCalculateButton() {
		waitForVisibilityOf(calcRewardsBtn);
		calcRewardsBtn.click();
		explicitWait(1);
	}

	public boolean clickAvgSpendTabAndCheck() {
		waitForElementToBeClickable(averageTabTrigger);
		averageTabTrigger.click();
		explicitWait(0.2);
		return isAverageSpendTabActive();
	}

	public String getAverageSpendValueText() {
		waitForVisibilityOf(averageSpendInput);
		return averageSpendInput.getAttribute("value").trim();
	}

	public boolean clickDetailedSpendTabAndCheck() {
		waitForElementToBeClickable(detailedTabTrigger);
		detailedTabTrigger.click();
		explicitWait(0.2);
		return isDetailedSpendTabActive();
	}

	public void enterAverageSpend(String amount) {
		waitForVisibilityOf(averageSpendInput);
		averageSpendInput.click();
		averageSpendInput.sendKeys(Keys.CLEAR);
		String digitsOnly = amount.replaceAll("[^0-9]", "");
		averageSpendInput.sendKeys(digitsOnly);
		averageSpendInput.sendKeys(Keys.TAB);
	}

	public String getTotalMonthlySpendText() {
		waitForVisibilityOf(totalMonthlySpend);
		return totalMonthlySpend.getText().trim();
	}

	public void enterDetailedSpend(String category, String amount) {
		switch (category.trim().toLowerCase()) {
		case "groceries and drug stores":
			setAmount(groceriesInput, amount);
			break;
		case "bill payments":
			setAmount(billsInput, amount);
			break;
		case "entertainment and dining":
			setAmount(entertainmentInput, amount);
			break;
		case "transportation":
			setAmount(transportationInput, amount);
			break;
		case "home improvements":
			setAmount(homeImprovementInput, amount);
			break;
		case "travel":
			setAmount(travelInput, amount);
			break;
		case "all other purchases":
			setAmount(otherPurchasesInput, amount);
			break;
		default:
			break;
		}
	}

	// generic function to enter amount in different web elements in detailed spend
	private void setAmount(WebElement input, String amount) {
		waitForVisibilityOf(input);
		input.click();
		input.sendKeys(Keys.CLEAR);
		input.sendKeys(amount);
		input.sendKeys(Keys.TAB);
	}

}
