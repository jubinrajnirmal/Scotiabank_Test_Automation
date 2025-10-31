#Author: siddharth.dalwadi@fdmgroup.com
#Keywords Summary : Verify Auto Loan Calculator Functionality
@AutoLoanCalculatorFeature
Feature: Auto Loan Calculator

  Background: 
    Given User is on the Home Page
    And User handles Cookie banner pop-up

  @AutoLoanCalculator
  Scenario Outline: Verify the functionality of autoloan calculator
    And User hovers over the "Loans & Lines of Credit" menu
    When User clicks on "Auto loan payment calculator"
    Then User is navigated to the Auto loan payment calculator page
    When User enters <loan> loam amount
    And enters <DownPayment> as down payment
    And enters <Months> months as loan duration
    And enters <IntRate> as interest rate
    Then total amount is calculated

    Examples:
      | loan  | DownPayment | Months | IntRate |
      | 25000 | 100 | 96 | 2.5 |
      | 30000 | 1000 | 48 | 3.5 |
      | 35000 | 5000 | 72 | 4.99 |
      | 80000 | 15000 | 96 | 7.49 |

  Scenario Outline: Verify the functionality of Mortgage calculator
    And User hovers over the "Mortgages" menu
    When User clicks on "Mortgage calculator"
    Then User is navigated to the Mortgage Calculator page
    When Users enters <MortgageAmount> as mortgage amount
    And enters <AmortizationPeriod> years of Amortization period
    And enters custon interest rate of <InterestRate>
    Then receives vaid EMI amount

    Examples:
      | MortgageAmount | AmortizationPeriod | InterestRate |
      | 400000 | 20 | 1.99 |
      | 550000 | 25 | 4.99 |
      | 750000 | 30 | 5.49 |
