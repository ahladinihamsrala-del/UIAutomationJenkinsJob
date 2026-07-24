package api.automation.usermanagement.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public final class ExcelFileUtil {
	private static final DataFormatter FORMATTER = new DataFormatter();

	private ExcelFileUtil() {
	}

//To read the excel file.Reads the User ID and status  and returns the header and value pairs in a map
	public static Map<String, String> readByTestCase(Path file, String sheetName, String testCase) throws IOException {

		if (!Files.exists(file)) {
			throw new IllegalArgumentException("Input Excel file was not found: " + file);
		}

		try (Workbook workbook = openExistingWorkbook(file)) {
			Sheet sheet = workbook.getSheet(sheetName);

			if (sheet == null) {
				throw new IllegalArgumentException("Excel sheet was not found: " + sheetName);
			}

			Row header = sheet.getRow(0);

			if (header == null) {
				throw new IllegalArgumentException("Excel header row was not found.");
			}

			for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {

				Row row = sheet.getRow(rowIndex);//fetching rows one by one 

				if (row == null) {
					continue;
				}

				Map<String, String> values = readRow(header, row);

				
				String actualTestCase = values.get(normalize("TestCase"));//extracting the TC data from the values map 

				//System.out.println("Excel keys: " + values.keySet());
				//System.out.println("From Excel: [" + actualTestCase + "]");

				if (actualTestCase != null
				        && normalize(testCase).equals(normalize(actualTestCase))) {
				    System.out.println("Fetched from Excel: " + actualTestCase);
				    return values;
				}
				if (testCase.equalsIgnoreCase(actualTestCase)) {
					System.out.println("fetched from Excel " + values.get("TestCase"));
					return values;// if the test case id matches in excel that row values are returned
				}
			}
		}

		throw new IllegalArgumentException("TestCase was not found in Excel: " + testCase);
	}

	// To write the response received to the Resuts.xlx file
	public static synchronized void appendResult(Path file, String sheetName, Map<String, String> result)
			throws IOException {

		if (file.getParent() != null) {
			Files.createDirectories(file.getParent());
		}

		try (Workbook workbook = openWorkbook(file)) {
			Sheet sheet = workbook.getSheet(sheetName);

			if (sheet == null) {
				sheet = workbook.createSheet(sheetName);
			}

			Row header = sheet.getRow(0);

			if (header == null) {
				header = sheet.createRow(0);
			}

			Map<String, Integer> columns = getExistingColumns(header);

			int nextColumn = columns.size();

			for (String key : result.keySet()) {
				if (!columns.containsKey(key)) {
					columns.put(key, nextColumn);
					header.createCell(nextColumn).setCellValue(key);
					nextColumn++;
				}
			}

			Row output = sheet.createRow(sheet.getLastRowNum() + 1);

			for (Map.Entry<String, String> entry : result.entrySet()) {
				int column = columns.get(entry.getKey());

				output.createCell(column).setCellValue(trimForExcel(entry.getValue()));
			}

			try (OutputStream outputStream = Files.newOutputStream(file)) {
				workbook.write(outputStream);
			}
		}
	}

	private static Map<String, String> readRow(Row header, Row row) {

		Map<String, String> values = new LinkedHashMap<>();

		int columnCount = header.getLastCellNum();

		for (int column = 0; column < columnCount; column++) {

			String headerName = normalize(cellText(header.getCell(column)));

			if (!headerName.isBlank()) {
				values.put(headerName, cellText(row.getCell(column)));
			}
		}

		return values;
	}

	private static Map<String, Integer> getExistingColumns(Row header) {

		Map<String, Integer> columns = new LinkedHashMap<>();

		for (Cell cell : header) {
			columns.put(cellText(cell), cell.getColumnIndex());
		}

		return columns;
	}

	private static Workbook openExistingWorkbook(Path file) throws IOException {

		try (InputStream input = Files.newInputStream(file)) {
			return WorkbookFactory.create(input);
		}
	}

	private static Workbook openWorkbook(Path file) throws IOException {

		if (Files.exists(file) && Files.size(file) > 0) {
			return openExistingWorkbook(file);
		}

		return new XSSFWorkbook();
	}

	private static String cellText(Cell cell) {
		return cell == null ? "" : FORMATTER.formatCellValue(cell).trim();
	}

	private static String normalize(String value) {
		return value == null ? "" : value.toLowerCase().replaceAll("[^a-z0-9]", "");
	}

	private static String trimForExcel(String value) {
		if (value == null) {
			return "";
		}

		return value.length() > 32700 ? value.substring(0, 32700) : value;
	}
}
