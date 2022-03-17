package com.endava.tmd.soj.ps.excel.exporter;

import java.util.List;
import java.util.Map.Entry;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.endava.tmd.soj.ps.model.Data;
import com.endava.tmd.soj.ps.model.ProductHistory;

public class ExcelExporter {
    private final Data data;
    private final int startRow;
    private final int startCol;

    public ExcelExporter(final Data data, final int startRow, final int startCol) {
        this.data = data;
        this.startRow = startRow;
        this.startCol = startCol;
    }

    public XSSFWorkbook createWorkbook() {
        final var workbook = new XSSFWorkbook();
        final var sheet = workbook.createSheet("Report");
        exportHeader(workbook, sheet);
        exportProducts(workbook, sheet);
        return workbook;
    }

    private void exportHeader(final XSSFWorkbook workbook, final XSSFSheet sheet) {
        new ExcelHeaderExporter(workbook, sheet, startCol).export(startRow, data.getHeaders());
    }

    private void exportProducts(final XSSFWorkbook workbook, final XSSFSheet sheet) {
        final List<ExcelLineExporter> exporters = List.of(
                new ExcelLineEvenExporter(workbook, sheet, startCol),
                new ExcelLineOddExporter(workbook, sheet, startCol));
        var productIndex = 0;

        for (final Entry<String, ProductHistory> entry : data.getProducts().entrySet()) {
            exporters.get(productIndex % 2).export(productIndex * 3 + startRow + 1, entry);
            productIndex++;
        }
    }

}
