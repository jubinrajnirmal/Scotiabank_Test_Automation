package com.scotiabank.utils;

import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class ExcelConfig {
	private static final Map<String, String> CONFIG = new HashMap<>();
	static {
		try (FileInputStream fis = new FileInputStream("src/test/resources/testdata/Scotibank_Data.xlsx")) {
			Workbook wb = WorkbookFactory.create(fis);
			Sheet s = wb.getSheet("Config"); // one sheet only
			DataFormatter f = new DataFormatter();
			for (int r = 1; r <= s.getLastRowNum(); r++) { // row 0 = headers
				Row row = s.getRow(r);
				if (row == null)
					continue;
				String k = f.formatCellValue(row.getCell(0)).trim();
				String v = f.formatCellValue(row.getCell(1)).trim();
				if (!k.isEmpty())
					CONFIG.put(k, v);
			}
			wb.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	// Fetching URLs
	public static String get(String key) {
		return CONFIG.get(key);
	}

	// Fetching BrowserDriver
	public static String get(String module, String key) {
		return CONFIG.get(key);
	}
}