package com.scotiabank.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.scotiabank.utils.BrowserUtils;

public class CCProductsResultPage extends BrowserUtils {

	// Locators
	@FindBy(xpath = "//h2[contains(normalize-space(),'You spend') and contains(.,'typical month')]")
	private WebElement resultsText;
	@FindBy(xpath = "//div[contains(@class,'rewards-calc-container')]")
	private WebElement resultsContainer;

	// Helpers
	public boolean isResultsPageVisibile() {
		waitForVisibilityOf(resultsText);
		return resultsText.isDisplayed();
	}

	public String getResultText() {
		waitForVisibilityOf(resultsText);
		return resultsText.getText().trim();
	}

	public boolean areResultsVisible() {
		waitForVisibilityOf(resultsContainer);
		return resultsContainer.isDisplayed();

	}

}
