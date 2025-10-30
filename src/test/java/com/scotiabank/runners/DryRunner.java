package com.scotiabank.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@SuppressWarnings("deprecation")
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features/F1_CCRewardsCalculator.feature", // <-- exact file
    glue = { "com.scotiabank.stepdefs", "com.scotiabank.utils" },
    dryRun = true,
    tags = "", //<- add the tag to generate the stepdef methods
    plugin = { "pretty", "summary" },
    monochrome = true
)
public class DryRunner {}