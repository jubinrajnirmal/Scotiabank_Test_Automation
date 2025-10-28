package com.scotiabank.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@SuppressWarnings("deprecation")
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "classpath:features/HomePage_Menu.feature", // <-- exact file
    glue = { "com.rbcroyalbank.tests.stepdefinitions", "com.rbcroyalbank.framework.utils" },
    dryRun = true,
    tags = "", //<- add the tag to generate the stepdef methods
    plugin = { "pretty", "summary" },
    monochrome = true
)
public class DryRunner {}