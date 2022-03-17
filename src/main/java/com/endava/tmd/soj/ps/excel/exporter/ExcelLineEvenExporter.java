package com.endava.tmd.soj.ps.excel.exporter;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class ExcelLineEvenExporter extends ExcelLineExporter {

    ExcelLineEvenExporter(final XSSFWorkbook workbook, final XSSFSheet sheet, final int startCol) {
        super(workbook, sheet, startCol);
    }

    @Override
    byte[] getSpecificForegroundColor() {
        final var rgb = new byte[3];
        rgb[0] = (byte) 255; // red
        rgb[1] = (byte) 255; // green
        rgb[2] = (byte) 255; // blue
        return rgb;
    }

}
