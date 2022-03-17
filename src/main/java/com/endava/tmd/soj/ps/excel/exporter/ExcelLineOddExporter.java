package com.endava.tmd.soj.ps.excel.exporter;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class ExcelLineOddExporter extends ExcelLineExporter {

    ExcelLineOddExporter(final XSSFWorkbook workbook, final XSSFSheet sheet, final int startCol) {
        super(workbook, sheet, startCol);
    }

    @Override
    byte[] getSpecificForegroundColor() {
        final var rgb = new byte[3];
        rgb[0] = (byte) 240; // red
        rgb[1] = (byte) 240; // green
        rgb[2] = (byte) 240; // blue
        return rgb;
    }

}
