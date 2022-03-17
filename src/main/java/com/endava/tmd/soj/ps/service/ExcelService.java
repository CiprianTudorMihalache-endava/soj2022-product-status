package com.endava.tmd.soj.ps.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.endava.tmd.soj.ps.excel.exporter.ExcelExporter;
import com.endava.tmd.soj.ps.excel.importer.ExcelImporter;
import com.endava.tmd.soj.ps.exception.ExcelOperationException;
import com.endava.tmd.soj.ps.model.Data;

public class ExcelService {
    private static final int START_ROW = 1; // first row will be empty
    private static final int START_COL = 1; // first column will be empty

    public Data readExistingData(final File excelFile) {
        try (final var workbook = new XSSFWorkbook(excelFile)) {
            return new ExcelImporter(workbook, START_ROW, START_COL).performImport();
        } catch (final IOException | InvalidFormatException e) {
            throw new ExcelOperationException(e);
        }
    }

    public void saveData(final Data data, final File excelFile) {
        try (final var fos = new FileOutputStream(excelFile);
                final var workbook = new ExcelExporter(data, START_ROW, START_COL).createWorkbook()) {
            workbook.write(fos);
        } catch (final IOException e) {
            throw new ExcelOperationException(e);
        }
    }

}
