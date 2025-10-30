# Author: Jubin Raj Nirmal

@ui @scotia @rewards
Feature: Credit Card rewards calculator
  As a consumer shopping for a new credit card
  I want to estimate my rewards based on my monthly spending patterns
  So that I can compare Scotiabank cards and select the best option for me

  Background:
    Given the user has navigated to the Scotiabank credit card rewards calculator

  @smoke
  Scenario: Display of Detailed spend and Average spend tabs
    When the calculator section is visible
    Then the user should see the tabs "Detailed spend" and "Average spend"
    And the "Detailed spend" tab should be active by default
    And the URL trail should be "#/detailed-spend"
    And the calculator should list spending categories "Groceries and drug stores"
    And the calculator should list spending categories "Bill payments"
    And the calculator should list spending categories "Entertainment and dining"
    And the calculator should list spending categories "Transportation"
    And the calculator should list spending categories "Home improvements"
    And the calculator should list spending categories "Travel"
    And the calculator should list spending categories "All other purchases"
    And the "Calculate rewards" button should be enabled

  @avgSpend
  Scenario: Calculate rewards using average spend amount
    Given the user selects the "Average spend" tab
    When the user enters "1500" as the average monthly spend
    And the user clicks the "Calculate rewards" button
    Then a results page should be displayed
    And the URL trail should be "#/rewards-calc"
    And it should state "You spend $1,500 on your credit card in a typical month."
    And recommended credit card products should be displayed

  @detailedSpend
  Scenario Outline: Calculate rewards using detailed spend values
    Given the user is on the "Detailed spend" tab
    When the user enters "<groceries>" in "Groceries and drug stores"
    And the user enters "<bills>" in "Bill payments"
    And the user enters "<entertainment>" in "Entertainment and dining"
    And the user enters "<transportation>" in "Transportation"
    And the user enters "<home_improvements>" in "Home improvements"
    And the user enters "<travel>" in "Travel"
    And the user enters "<other_purchases>" in "All other purchases"
    And the total monthly spend is displayed
    And the user clicks the "Calculate rewards" button
    Then a results page should be displayed
    And it should summarise the total monthly spend as "$<total>"

    Examples:
      | groceries | bills | entertainment | transportation | home_improvements | travel | other_purchases | total |
      | 300 | 200 | 150 | 100 | 50 | 80 | 70 | 950 |
      | 400 | 400 | 250 | 200 | 150 | 120 | 50 | 1570 |
      | 50 | 75 | 25 | 40 | 0 | 0 | 10 | 200 |
      | 500 | 300 | 200 | 150 | 100 | 250 | 175 | 1675 |
      | 999 | 0 | 0 | 0 | 0 | 0 | 0 | 999 |
      | 1200 | 600 | 400 | 250 | 350 | 500 | 300 | 3600 |
      | 80 | 220 | 90 | 60 | 30 | 40 | 55 | 575 |
      | 200 | 200 | 200 | 200 | 200 | 200 | 200 | 1400 |
      | 145 | 180 | 135 | 95 | 60 | 110 | 75 | 800 |


  @avgSpendDDT
  Scenario Outline: Calculate rewards with different average spend values
    Given the user is on the "Average spend" tab
    When the user enters "<spend>" as the average monthly spend
    And the user clicks the "Calculate rewards" button
    Then a results page should be displayed
    And it should state "You spend $<spend> on your credit card in a typical month"

    Examples:
      | spend |
      | 250 |
      | 1,500 |
      | 2,000 |
      | 3,500 |
      | 2,222 |
      | 3,333 |
      | 4,500 |
      | 1,234 |
	  
  @edgeZero
    Scenario: Zero total should not navigate to results
  	  Given the user is on the "Detailed spend" tab
  	  When the user enters "0" in "Groceries and drug stores"
  	  And the user enters "0" in "Bill payments"
  	  And the user enters "0" in "Entertainment and dining"
  	  And the user enters "0" in "Transportation"
  	  And the user enters "0" in "Home improvements"
      And the user enters "0" in "Travel"
      And the user enters "0" in "All other purchases"
      And the total monthly spend is displayed
      And the user clicks the "Calculate rewards" button
      Then the results page should NOT be displayed
      And the calculator should show total monthly spend "$0"
      
  @avgZero
	Scenario: Average spend = 0 should not navigate to results
  		Given the user has navigated to the Scotiabank credit card rewards calculator
  		And the user is on the "Average spend" tab
  		When the user enters "0" as the average monthly spend
 		And the user clicks the "Calculate rewards" button
  		Then the results page should NOT be displayed
  		And the average spend field should show "$0"       
      
      
      
  
