package com.scotiabank.runners;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ GenerateFeaturesTask.class, // first gen features
		CucumberRunner.class // then execute cucumber tests
})
public class CucumberSuiteRunner {
}
