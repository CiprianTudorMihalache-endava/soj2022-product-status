package com.endava.tmd.soj.ps.excel.exporter;

import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class ExcelHeaderExporter {
    private static final String PRODUCT_HEADER = "Product";

    private final XSSFWorkbook workbook;
    private final XSSFSheet sheet;
    private final int startCol;
    private final CellStyle cellStyle;

    ExcelHeaderExporter(final XSSFWorkbook workbook, final XSSFSheet sheet, final int startCol) {
        this.workbook = workbook;
        this.sheet = sheet;
        this.startCol = startCol;
        cellStyle = createHeaderCellStyle();
    }

    public void export(final int rowNb, final List<String> headers) {
        fillHeader(rowNb, headers);
        setColumnWidths(headers.size());
    }

    private CellStyle createHeaderCellStyle() {
        final var font = workbook.createFont();
        font.setBold(true);
        font.setItalic(true);
        font.setColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        final var cs = workbook.createCellStyle();
        cs.setFont(font);
        cs.setAlignment(HorizontalAlignment.CENTER);
        cs.setVerticalAlignment(VerticalAlignment.CENTER);
        cs.setBorderLeft(BorderStyle.MEDIUM);
        cs.setBorderBottom(BorderStyle.MEDIUM);
        cs.setBorderRight(BorderStyle.MEDIUM);
        cs.setBorderTop(BorderStyle.MEDIUM);
        cs.setFillPattern(FillPatternType.FINE_DOTS);
        cs.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cs.setFillBackgroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
        cs.setWrapText(true);
        return cs;
    }

    private void fillHeader(final int rowNb, final List<String> headers) {
        final var row = sheet.createRow(rowNb);
        row.setHeightInPoints(32);

        var cell = row.createCell(startCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(PRODUCT_HEADER);

        for (var i = 0; i < headers.size(); i++) {
            cell = row.createCell(startCol + 1 + i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(headers.get(i));
        }
    }

    private void setColumnWidths(final int nbDateHeaders) {
        for (var i = 0; i < startCol; i++) {
            sheet.setColumnWidth(i, 3 * 256);
        }
        sheet.setColumnWidth(startCol, 40 * 256);
        for (var i = 0; i < nbDateHeaders; i++) {
            sheet.setColumnWidth(startCol + 1 + i, 12 * 256);
        }
    }

}
