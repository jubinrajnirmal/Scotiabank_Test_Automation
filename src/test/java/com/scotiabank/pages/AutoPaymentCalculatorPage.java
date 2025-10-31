package com.scotiabank.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.scotiabank.utils.BrowserUtils;

public class AutoPaymentCalculatorPage extends BrowserUtils {

	@FindBy(xpath = "//h1[contains(text(),'Auto loan payment calculator')]")
	private WebElement calculatorPageTitle;

	@FindBy(xpath = "//iframe[contains(@title, 'Auto loan')]")
	private WebElement autoPaymentCalculatorIframe;

	@FindBy(xpath = "//input[contains(@id, 'slider-input-mpc-cp')]")
	private WebElement priceInputBox;

	@FindBy(xpath = "//input[contains(@id, 'slider-input-mpc-tia')]")
	private WebElement tradeInValueInputBox;

	@FindBy(xpath = "//input[contains(@id, 'slider-input-mpc-lo')]")
	private WebElement existingLoanInputBox;

	@FindBy(xpath = "//input[contains(@id, 'slider-input-mpc-dp')]")
	private WebElement downPaymentInputBox;

	@FindBy(xpath = "//input[contains(@id, 'slider-input-mpc-cl')]")
	private WebElement durationOfLoanInputBox;

	@FindBy(xpath = "//input[contains(@id, 'slider-input-mpc-ir')]")
	private WebElement interestRateInputBox;

	@FindBy(id= "total")
	private WebElement totalPriceElement;
	
	@FindBy(xpath="//td[contains(@id, 'monthly-mp')]")
	private WebElement monthlyEMIElement;
	
	@FindBy(xpath="//body")
	private WebElement bodyElement;
	
	private int vehiclePrice;
	private int downPayment;
	private int durationMonths;
	private double annualInterestRate;
	
	public boolean verifyCalculatorPageTileIsDisplayed() {
		return calculatorPageTitle.isDisplayed();
	}

	public void switchToAutoPaymentCalculator() {
		driver.switchTo().frame(autoPaymentCalculatorIframe);
	}

	public void switchToDefaultFrame() {
		driver.switchTo().defaultContent();
	}

	public void enterPriceOfVehicle(int price) {
		priceInputBox.clear();
		priceInputBox.sendKeys(String.valueOf(price));
		vehiclePrice =price;
	}

	public void enterPriceOfTradeInVehicle(int price) {
		tradeInValueInputBox.clear();
		tradeInValueInputBox.sendKeys(String.valueOf(price));
	}

	public void enterExistingLoanAmount(Integer loanAmt) {
		existingLoanInputBox.clear();
		existingLoanInputBox.sendKeys(String.valueOf(loanAmt));
		
	}

	public void enterDownPaymentAmount(Integer DownPaymentAmt) {
		downPaymentInputBox.clear();
		downPaymentInputBox.sendKeys(String.valueOf(DownPaymentAmt));
		downPayment = DownPaymentAmt;
	}

	public void enterLoanDuration(Integer durationOfMonths) {
		durationOfLoanInputBox.clear();
		durationOfLoanInputBox.sendKeys(String.valueOf(durationOfMonths));
		durationMonths = durationOfMonths;
	}

	public void enterInterestRate(Double interestRate) {
		interestRateInputBox.clear();
		interestRateInputBox.sendKeys(String.valueOf(interestRate));
		annualInterestRate=interestRate;
	}

	public double getTotalValue() {
		String totalText = totalPriceElement.getText();
		System.out.println("Raw total from site: " + totalText);
		
		String cleanText = totalText.replaceAll("[^0-9.]", ""); // removes everything except digits and dot
		double totalValue = Double.parseDouble(cleanText);

		System.out.println("Numeric total value after clean : " + totalValue);
		return totalValue;
	
	}

	public double getMonthlyEMIFromSite() {
		String emiText = monthlyEMIElement.getText();
		System.out.println("Raw EMI from site: " + emiText);
		
		String cleanText = emiText .replaceAll("[^0-9.]", ""); // removes everything except digits and dot
		double emiValue = Double.parseDouble(cleanText);

		System.out.println("Numeric total value after clean : " + emiValue );
		return emiValue ;
	
	}

	public double getCalculatedEmiAmount() {

		System.out.println("Vehicle Price: " + vehiclePrice);
		System.out.println("Down Payment: " + downPayment);
		System.out.println("Annual Interest Rate: " + annualInterestRate);
		System.out.println("Duration (months): " + durationMonths);
		

	    double principal = (vehiclePrice*1.13 ) - downPayment;
		double monthlyRate = (annualInterestRate / 12)/100;

		double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, durationMonths))
				/ (Math.pow(1 + monthlyRate, durationMonths) - 1);

		return Math.round(emi * 100.0) / 100.0;
	}
	
	public void clickTotalElement() {
		totalPriceElement.click();
	}
	
	public double getCalculatedTotalAmount() {

		System.out.println("Vehicle Price: " + vehiclePrice);
		System.out.println("Down Payment: " + downPayment);
		System.out.println("Annual Interest Rate: " + annualInterestRate);
		System.out.println("Duration (months): " + durationMonths);
		

	    double principal = (vehiclePrice *1.13) - downPayment;
		double monthlyRate = (annualInterestRate / 12)/100;

		double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, durationMonths))
				/ (Math.pow(1 + monthlyRate, durationMonths) - 1);

		double totalAmount = emi * durationMonths;
		
		double totalWithHST = totalAmount * 1.13;

	    // Round to 2 decimals
	    return Math.round(totalAmount * 100.0) / 100.0;
	}

}
