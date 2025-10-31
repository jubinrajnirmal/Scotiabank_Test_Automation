package com.scotiabank.stepdefs;

import org.junit.Assert;

import com.scotiabank.pages.AutoPaymentCalculatorPage;
import com.scotiabank.pages.HomePage;
import com.scotiabank.pages.MortgageCalculator;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AutoLoanCalculatorStepDef {

	HomePage homePage = new HomePage();
	AutoPaymentCalculatorPage autoPaymentCalculatorPage = new AutoPaymentCalculatorPage();
	MortgageCalculator mortgageCalculator = new MortgageCalculator();

	@Given("User is on the Home Page")
	public void user_is_on_the_home_page() {
		Assert.assertTrue(homePage.verifyHomePageLogoIsDisplayed());
	}

	@Given("User handles Cookie banner pop-up")
	public void user_handles_cookie_banner_pop_up() {
		homePage.closeCookieBannerIfPresent();
	}

	@Given("User hovers over the {string} menu")
	public void user_hovers_over_the_menu(String menuName) throws InterruptedException {
		homePage.hoverOverSelectedMenuOption(menuName);
		Thread.sleep(3000);
	}

	@When("User clicks on {string}")
	public void user_clicks_on(String sumMenuItem) {
		homePage.selectSubMenuItem(sumMenuItem);
	}

	@Then("User is navigated to the Auto loan payment calculator page")
	public void user_is_navigated_to_the_auto_loan_payment_calculator_page() {
		autoPaymentCalculatorPage.verifyCalculatorPageTileIsDisplayed();
		autoPaymentCalculatorPage.switchToAutoPaymentCalculator();
	}

	@When("User enters {int} loam amount")
	public void user_enters_loam_amount(Integer loanAmount) {
		autoPaymentCalculatorPage.enterPriceOfVehicle(loanAmount);
		autoPaymentCalculatorPage.enterExistingLoanAmount(0);
	}

	@When("enters {int} as down payment")
	public void enters_as_down_payment(Integer downPayment) {
		autoPaymentCalculatorPage.enterDownPaymentAmount(downPayment);
	}

	@When("enters {int} months as loan duration")
	public void enters_months_as_loan_duration(Integer months) {
		autoPaymentCalculatorPage.enterLoanDuration(months);
	}

	@When("enters {double} as interest rate")
	public void enters_as_interest_rate(Double interestRate) {
		autoPaymentCalculatorPage.enterInterestRate(interestRate);
	}

	@SuppressWarnings("deprecation")
	@Then("total amount is calculated")
	public void total_amount_is_calculated() throws InterruptedException {

		autoPaymentCalculatorPage.clickTotalElement();
		double calculatedEMI = autoPaymentCalculatorPage.getCalculatedEmiAmount();
		double siteEMI = autoPaymentCalculatorPage.getMonthlyEMIFromSite();
		System.out.println("calculated Total: " + calculatedEMI);
		System.out.println("Site Total: " + siteEMI);

		Assert.assertEquals(calculatedEMI, siteEMI, 0.01);

	}

	@Then("User is navigated to the Mortgage Calculator page")
	public void user_is_navigated_to_the_mortgage_calculator_page() {
		Assert.assertTrue(mortgageCalculator.verifyTitleIsDisplayed());
		mortgageCalculator.switchToMortgageCalculatorFrame();
	}

	@When("Users enters {int} as mortgage amount")
	public void users_enters_as_mortgage_amount(Integer mortgageAmount) throws InterruptedException {
		mortgageCalculator.enterMortgageAmount(mortgageAmount);
	}

	@When("enters {int} years of Amortization period")
	public void enters_years_of_amortization_period(Integer amortizationPeriod) throws InterruptedException {
		mortgageCalculator.enterAmortizationYears(amortizationPeriod);
	}

	@When("enters custon interest rate of {double}")
	public void enters_custon_interest_rate_of(Double interestRate) throws InterruptedException {
		mortgageCalculator.enterInterestRate(interestRate);
	}

	@Then("receives vaid EMI amount")
	public void receives_vaid_emi_amount() throws InterruptedException {
		Thread.sleep(5000);
		Assert.assertEquals(mortgageCalculator.calculateMonthlyPayment(), mortgageCalculator.getMonthlyPaymentAmount(),
				100.01);
	}

}
