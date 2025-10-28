package com.scotiabank.utils;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;

public class ExcelExamplesGenerator {

	private static final String IN_DIR = "src/test/resources/features";
	private static final String OUT_DIR = "src/test/resources/features/_generated";
	private static final String XLSX = "src/test/resources/testdata/Scotiabank_Data.xlsx"; 

	public static void main(String[] args) throws Exception {
		Path in = Paths.get(IN_DIR);
		Path out = Paths.get(OUT_DIR);
		Files.createDirectories(out);

		DataFormatter fmt = new DataFormatter();

		try (DirectoryStream<Path> ds = Files.newDirectoryStream(in, "*.feature")) {
			for (Path f : ds) {
				List<String> lines = Files.readAllLines(f, StandardCharsets.UTF_8);
				List<String> outLines = new ArrayList<>();

				for (int i = 0; i < lines.size(); i++) {
					String line = lines.get(i);

					if (line.trim().equals("Examples:")) {
						String indent = line.substring(0, line.indexOf("Examples:"));
						String marker = lines.get(i + 1); 
						String sheet = marker.substring(marker.indexOf(':') + 1).trim();
						String header = lines.get(i + 2); 

						outLines.add(indent + "Examples:");
						outLines.add(header);

						// parse headers
						List<String> headers = new ArrayList<>();
						for (String part : header.split("\\|")) {
							String t = part.trim();
							if (!t.isEmpty())
								headers.add(t);
						}

						// dump rows from Excel
						try (FileInputStream fis = new FileInputStream(XLSX);
								Workbook wb = WorkbookFactory.create(fis)) {
							Sheet sh = wb.getSheet(sheet);
							int last = sh.getLastRowNum();
							for (int r = 1; r <= last; r++) {
								Row row = sh.getRow(r);
								if (row == null)
									continue;
								StringBuilder sb = new StringBuilder(indent + "  |");
								for (int c = 0; c < headers.size(); c++) {
									String v = fmt.formatCellValue(row.getCell(c)).trim();
									sb.append(' ').append(v).append(" |");
								}
								outLines.add(sb.toString());
							}
						}

						// skip marker, header, and any placeholder rows below
						i += 2;
						while (i + 1 < lines.size() && lines.get(i + 1).trim().startsWith("|"))
							i++;
						continue;
					}

					outLines.add(line);
				}

				Files.write(out.resolve(f.getFileName()), outLines, StandardCharsets.UTF_8);
			}
		}
		System.out.println("Generated features in: " + Paths.get(OUT_DIR).toAbsolutePath());
	}

}