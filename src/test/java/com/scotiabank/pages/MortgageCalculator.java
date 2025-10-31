package com.scotiabank.pages;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.scotiabank.utils.BrowserUtils;

public class MortgageCalculator extends BrowserUtils {

	@FindBy(xpath = "//div[contains(@class,'center-mobile ')]//h1[contains(text(),'Mortgage Calculator')]")
	private WebElement mortgageCalculatorPageTitle;

	@FindBy(xpath = "//iframe[contains(@src,'mortgagecalculator') and contains(@title,'Mortgage Calculator')]")
	private WebElement mortgageCalculatorFrame;

	@FindBy(xpath = "//input[@id='mortamm1' and @data-format='currency']")
	private WebElement mortgageAmountInput;

	@FindBy(xpath = "//input[@id='amortm1' and @data-format='year']")
	private WebElement amortizationYearsInput;

	@FindBy(xpath = "//input[@id='interestratem1' and @data-format='percent']")
	private WebElement interestRateInput;

	@FindBy(xpath = "//span[contains(@class,'paymentM1') and contains(text(),'monthly')]")
	private WebElement monthlyPaymentLabel;

	private double principal;
	private double annualInterestRate;
	private int amortizationYears;

	public boolean verifyTitleIsDisplayed() {
		return mortgageCalculatorPageTitle.isDisplayed();
	}

	public void switchToMortgageCalculatorFrame() {
		driver.switchTo().frame(mortgageCalculatorFrame);
	}

	public void enterMortgageAmount(double amount) throws InterruptedException {
		/*
		 * mortgageAmountInput.clear(); Thread.sleep(3000);
		 * 
		 * mortgageAmountInput.sendKeys(String.valueOf(amount));
		 */
		safelyEnterValues(mortgageAmountInput, String.valueOf(amount));

		System.out.println("Entered Mortgage Amount: " + amount);
		principal = amount;
	}

	public void enterAmortizationYears(int years) throws InterruptedException {

		/*
		 * amortizationYearsInput.clear(); Thread.sleep(3000);
		 * amortizationYearsInput.sendKeys(String.valueOf(years));
		 */

		safelyEnterValues(amortizationYearsInput, String.valueOf(years));
		System.out.println("Entered Amortization Period: " + years + " years");
		amortizationYears = years;
	}

	public void enterInterestRate(double rate) throws InterruptedException {
		/*
		 * interestRateInput.clear(); Thread.sleep(2000);
		 * interestRateInput.sendKeys(String.valueOf(rate));
		 */
		safelyEnterValues(interestRateInput, String.valueOf(rate));
		System.out.println("Entered Interest Rate: " + rate + "%");
		annualInterestRate = rate;
		// monthlyPaymentLabel.click();
	}

	public double calculateMonthlyPayment() {
		
		  double monthlyRate = (annualInterestRate / 12) / 100;
		  
		  int totalMonths = (amortizationYears * 12);
		  
		  double monthlyPayment = (principal * monthlyRate * Math.pow(1 + monthlyRate,
		  totalMonths)) / (Math.pow(1 + monthlyRate, totalMonths) - 1);
		  
		  return Math.round(monthlyPayment * 100.0) / 100.0;
		 

		/*
		 * BigDecimal rate =
		 * BigDecimal.valueOf(annualInterestRate).divide(BigDecimal.valueOf(12 * 100),
		 * 10, RoundingMode.HALF_EVEN); int totalMonths = amortizationYears * 12;
		 * 
		 * BigDecimal onePlusR = rate.add(BigDecimal.ONE); BigDecimal numerator =
		 * BigDecimal.valueOf(principal).multiply(rate).multiply(onePlusR.pow(
		 * totalMonths)); BigDecimal denominator =
		 * onePlusR.pow(totalMonths).subtract(BigDecimal.ONE);
		 * 
		 * BigDecimal monthly = numerator.divide(denominator, 10,
		 * RoundingMode.HALF_EVEN); BigDecimal rounded = monthly.setScale(2,
		 * RoundingMode.HALF_EVEN);
		 * 
		 * return rounded.doubleValue();
		 */
	}

	public double getMonthlyPaymentAmount() {

		String rawText = monthlyPaymentLabel.getText(); // e.g. "$3,210 monthly"
		System.out.println("Raw monthly payment text: " + rawText);
		String cleanText = rawText.replaceAll("[^0-9.]", "");
		double monthlyAmount = Double.parseDouble(cleanText);

		System.out.println("Parsed monthly payment: " + monthlyAmount);
		return monthlyAmount;
	}

	private void safelyEnterValues(WebElement element, String value) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);

		element.click();

		// Select all text and clear it quickly
		element.sendKeys(Keys.CONTROL + "a");
		element.sendKeys(Keys.BACK_SPACE);

		// Short delay to allow script to settle
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}

		// Immediately type new value
		element.sendKeys(value);

		// Trigger blur to confirm entry
		((JavascriptExecutor) driver).executeScript("arguments[0].blur();", element);

	}

}
