package com.scotiabank.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@SuppressWarnings("deprecation")
@RunWith(Cucumber.class)
@CucumberOptions(features = "file:src/test/resources/features/_generated", // filesystem path
		glue = { "com.scotiabank.stepdefs", "com.scotiabank.utils", "com.scotiabank.pages" }, plugin = {
				"pretty", "html:reports/cucumber.html", "json:reports/cucumber.json",
				"io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" }, publish = true, tags = "@ui and @scotia and @rewards" ,monochrome = true)
public class CucumberRunner {}
