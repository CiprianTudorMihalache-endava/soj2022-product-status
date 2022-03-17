package com.endava.tmd.soj.ps.excel.importer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.endava.tmd.soj.ps.model.Data;
import com.endava.tmd.soj.ps.model.ProductHistoricalStatus;
import com.endava.tmd.soj.ps.model.ProductHistory;

public class ExcelImporter {
    private final XSSFWorkbook workbook;
    private final int startRow;
    private final int startCol;

    public ExcelImporter(final XSSFWorkbook workbook, final int startRow, final int startCol) {
        this.workbook = workbook;
        this.startRow = startRow;
        this.startCol = startCol;
    }

    public Data performImport() {
        final var sheet = workbook.getSheetAt(0);
        final var data = new Data();
        data.setHeaders(readHeaders(sheet));
        data.setProducts(readProducts(sheet));
        return data;
    }

    private List<String> readHeaders(final XSSFSheet sheet) {
        final var row = sheet.getRow(startRow);
        final var headers = new ArrayList<String>();
        for (var i = startCol + 1; i < row.getLastCellNum(); i++) { // Strict less than
            headers.add(row.getCell(i).getStringCellValue());
        }
        return headers;
    }

    private Map<String, ProductHistory> readProducts(final XSSFSheet sheet) {
        final var products = new HashMap<String, ProductHistory>();
        for (var i = startRow + 1; i <= sheet.getLastRowNum(); i += 3) { // Less or equal
            final var link = sheet.getRow(i).getCell(1).getHyperlink().getAddress();
            products.put(link, readProduct(sheet, i));
        }
        return products;
    }

    private ProductHistory readProduct(final XSSFSheet sheet, final int rowNum) {
        final var productHistory = new ProductHistory();
        productHistory.setTitle(sheet.getRow(rowNum).getCell(startCol).getStringCellValue());
        productHistory.setStatusHistory(readStatusHistory(sheet, rowNum));
        return productHistory;
    }

    private List<ProductHistoricalStatus> readStatusHistory(final XSSFSheet sheet, final int rowNum) {
        final var history = new ArrayList<ProductHistoricalStatus>();
        final var firstRow = sheet.getRow(rowNum);
        for (var i = startCol + 1; i < firstRow.getLastCellNum(); i++) {
            history.add(new ProductHistoricalStatus()
                    .setPrice(firstRow.getCell(i).getNumericCellValue())
                    .setAvailable("Disponibil".equals(sheet.getRow(rowNum + 1).getCell(i).getStringCellValue()))
                    .setAvailabilityLabel(sheet.getRow(rowNum + 2).getCell(i).getStringCellValue()));
        }
        return history;
    }
}
