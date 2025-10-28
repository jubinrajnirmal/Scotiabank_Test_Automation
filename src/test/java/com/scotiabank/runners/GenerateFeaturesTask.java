package com.scotiabank.runners;

import com.scotiabank.utils.ExcelExamplesGenerator;
import org.junit.Test;

public class GenerateFeaturesTask {
    @Test
    public void generate() throws Exception {
        ExcelExamplesGenerator.main(new String[0]); // writes to features/_generated
    }
}
