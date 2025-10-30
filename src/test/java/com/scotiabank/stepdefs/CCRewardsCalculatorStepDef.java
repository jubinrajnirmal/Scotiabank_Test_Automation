package com.scotiabank.stepdefs;

import org.junit.Assert;

import com.scotiabank.pages.*;
import com.scotiabank.utils.BrowserUtils;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CCRewardsCalculatorStepDef extends BrowserUtils {

	// Page Objects
	CCRewardsCalculatorPage ccRewardsCalculatorPage = new CCRewardsCalculatorPage();
	CCProductsResultPage ccProductsResultPage = new CCProductsResultPage();

	@Given("the user has navigated to the Scotiabank credit card rewards calculator")
	public void the_user_has_navigated_to_the_scotiabank_credit_card_rewards_calculator() {
		ccRewardsCalculatorPage.navigateToCCRewardsCalculatorPage();
		Assert.assertTrue(ccRewardsCalculatorPage.isPageTitleVisible());
	}

	@When("the calculator section is visible")
	public void the_calculator_section_is_visible() {
		Assert.assertTrue(ccRewardsCalculatorPage.isCalculatorSectionVisible());
	}

	@Then("the user should see the tabs {string} and {string}")
	public void the_user_should_see_the_tabs_and(String string, String string2) {
		Assert.assertTrue(ccRewardsCalculatorPage.isDetailedSpendTabVisible());
		Assert.assertTrue(ccRewardsCalculatorPage.isDetailedSpendTabEnabled());
	}

	@Then("the {string} tab should be active by default")
	public void the_tab_should_be_active_by_default(String tabText) {
		boolean active = false;
		switch (tabText) {
		case "Detailed spend":
			active = ccRewardsCalculatorPage.isDetailedSpendTabActive();
			break;
		case "Average spend":
			active = ccRewardsCalculatorPage.isAverageSpendTabActive();
			break;
		default:
			break;
		}
		Assert.assertTrue("Expected active tab: " + tabText, active);
	}

	@Then("the URL trail should be {string}")
	public void trail(String expectedTrailText) {
		String actualTrailText = ccRewardsCalculatorPage.currentHash();
		Assert.assertEquals(actualTrailText, expectedTrailText);
	}

	@Then("the calculator should list spending categories {string}")
	public void the_calculator_should_list_spending_categories(String category) {
		Assert.assertTrue(ccRewardsCalculatorPage.isCategoryVisible(category));
	}

	@Then("the {string} button should be enabled")
	public void the_button_should_be_enabled(String calculatorBtn) {
		Assert.assertTrue(ccRewardsCalculatorPage.isCalculateButtonEnabled());
	}

	@Given("the user selects the {string} tab")
	public void the_user_selects_the_tab(String avgTab) {
		Assert.assertTrue(ccRewardsCalculatorPage.clickAvgSpendTabAndCheck());
	}

	@When("the user enters {string} as the average monthly spend")
	public void the_user_enters_as_the_average_monthly_spend(String avgAmount) {
		jsScrollDown();
		ccRewardsCalculatorPage.enterAverageSpend(avgAmount);
	}

	@When("the user clicks the {string} button")
	public void the_user_clicks_the_button(String btnText) {
		ccRewardsCalculatorPage.clickCalculateButton();
	}

	@Then("a results page should be displayed")
	public void a_results_page_should_be_displayed() {
		Assert.assertTrue(ccProductsResultPage.isResultsPageVisibile());
		jsScrollDown();
	}

	@Then("it should state {string}")
	public void it_should_state(String expected) {
		String actual = ccProductsResultPage.getResultText();
		String normActual = actual.replaceAll("\\s+", " ").trim();
		String normExpected = expected.replaceAll("\\s+", " ").trim();
		Assert.assertTrue(normActual.contains(normExpected));
	}

	@Then("recommended credit card products should be displayed")
	public void recommended_credit_card_products_should_be_displayed() {
		Assert.assertTrue(ccProductsResultPage.areResultsVisible());
	}

	@Given("the user is on the {string} tab")
	public void the_user_is_on_the_tab(String tabName) {
		boolean ok = false;
		switch (tabName.trim().toLowerCase()) {
		case "average spend":
			if (!ccRewardsCalculatorPage.isAverageSpendTabActive()) {
				Assert.assertTrue(ccRewardsCalculatorPage.clickAvgSpendTabAndCheck());
			}
			ok = ccRewardsCalculatorPage.isAverageSpendTabActive();
			break;
		case "detailed spend":
			if (!ccRewardsCalculatorPage.isDetailedSpendTabActive()) {
				Assert.assertTrue(ccRewardsCalculatorPage.clickDetailedSpendTabAndCheck());
			}
			ok = ccRewardsCalculatorPage.isDetailedSpendTabActive();
			break;
		default:
			break;
		}
		Assert.assertTrue(ok);
	}

	@When("the user enters {string} in {string}")
	public void the_user_enters_in(String amount, String category) {
		ccRewardsCalculatorPage.enterDetailedSpend(category, amount);
	}

	@When("the total monthly spend is displayed")
	public void the_total_monthly_spend_is_displayed() {
		String totalAmount = ccRewardsCalculatorPage.getTotalMonthlySpendText();
		Assert.assertNotNull(totalAmount);
		Assert.assertFalse(totalAmount.isEmpty());
	}

	@Then("it should summarise the total monthly spend as {string}")
	public void it_should_summarise_the_total_monthly_spend_as(String expectedValue) {
		String actualResults = ccProductsResultPage.getResultText();
		String actualDigits = actualResults.replaceAll("[^0-9]", "");
		String expectedDigits = expectedValue.replaceAll("[^0-9]", "");
		Assert.assertEquals(expectedDigits, actualDigits);
	}

	@Then("the results page should NOT be displayed")
	public void results_page_should_not_be_displayed() {
		boolean navigated = ccRewardsCalculatorPage.currentHash().contains("rewards-calc");
		Assert.assertFalse("Should not navigate to results when total is 0", navigated);
	}

	@Then("the calculator should show total monthly spend {string}")
	public void calculator_should_show_total_monthly_spend(String expected) {
		String actual = ccRewardsCalculatorPage.getTotalMonthlySpendText().replace(",", "");
		String exp = expected.replace(",", "");
		Assert.assertEquals("Calculator total mismatch", exp, actual);
	}

	@Then("the average spend field should show {string}")
	public void the_average_spend_field_should_show(String expected) {
		String actual = ccRewardsCalculatorPage.getAverageSpendValueText();
		String actDigits = actual.replaceAll("[^0-9]", "");
		String expDigits = expected.replaceAll("[^0-9]", "");
		Assert.assertEquals("Average spend input value mismatch", expDigits, actDigits);
	}

}
