package com.endava.tmd.soj.ps.excel.exporter;

import java.util.Map.Entry;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.endava.tmd.soj.ps.model.ProductHistory;

abstract class ExcelLineExporter {

    private final XSSFWorkbook workbook;
    private final XSSFSheet sheet;
    private final int startCol;
    private final CellStyle contentCellStyle;
    private final CellStyle linkCellStyle;

    ExcelLineExporter(final XSSFWorkbook workbook, final XSSFSheet sheet, final int startCol) {
        this.workbook = workbook;
        this.sheet = sheet;
        this.startCol = startCol;
        this.contentCellStyle = createContentCellStyle();
        this.linkCellStyle = createLinkCellStyle();
    }

    abstract byte[] getSpecificForegroundColor();

    public void export(final int startRow, final Entry<String, ProductHistory> product) {
        final var row1 = sheet.createRow(startRow);
        final var row2 = sheet.createRow(startRow + 1);
        final var row3 = sheet.createRow(startRow + 2);

        exportTitle(row1, row2, row3, startRow, product);
        exportHistory(row1, row2, row3, product);
    }

    private void exportTitle(final XSSFRow row1, final XSSFRow row2, final XSSFRow row3,
                             final int startRow, final Entry<String, ProductHistory> product) {
        final var link = workbook.getCreationHelper().createHyperlink(HyperlinkType.URL);
        link.setAddress(product.getKey());

        final var cellTitle1 = row1.createCell(startCol);
        cellTitle1.setCellStyle(linkCellStyle);
        cellTitle1.setCellValue(product.getValue().getTitle());
        cellTitle1.setHyperlink(link);

        final var cellTitle2 = row2.createCell(startCol);
        cellTitle2.setCellStyle(linkCellStyle);

        final var cellTitle3 = row3.createCell(startCol);
        cellTitle3.setCellStyle(linkCellStyle);

        sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 2, startCol, startCol));
    }

    private void exportHistory(final XSSFRow row1, final XSSFRow row2, final XSSFRow row3,
                               final Entry<String, ProductHistory> product) {
        final var statusHistory = product.getValue().getStatusHistory();
        for (var i = 0; i < statusHistory.size(); i++) {
            final var historicalStatus = statusHistory.get(i);
            final var crtCol = i + startCol + 1;

            final var cellPrice = row1.createCell(crtCol);
            cellPrice.setCellStyle(contentCellStyle);
            cellPrice.setCellValue(historicalStatus.getPrice());

            final var cellAvailable = row2.createCell(crtCol);
            cellAvailable.setCellStyle(contentCellStyle);
            cellAvailable.setCellValue(historicalStatus.isAvailable() ? "Disponibil" : "Indisponibil");

            final var cellAvLabel = row3.createCell(crtCol);
            cellAvLabel.setCellStyle(contentCellStyle);
            cellAvLabel.setCellValue(historicalStatus.getAvailabilityLabel());
        }
    }

    private CellStyle createContentCellStyle() {
        final var cs = createBaseCellStyle();
        cs.setAlignment(HorizontalAlignment.CENTER);
        return cs;
    }

    private CellStyle createLinkCellStyle() {
        final var font = workbook.createFont();
        font.setUnderline(Font.U_SINGLE);
        font.setColor(IndexedColors.BLUE.index);

        final var cs = createBaseCellStyle();
        cs.setFont(font);
        return cs;
    }

    private CellStyle createBaseCellStyle() {
        final var cs = workbook.createCellStyle();
        cs.setVerticalAlignment(VerticalAlignment.CENTER);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cs.setFillForegroundColor(new XSSFColor(getSpecificForegroundColor()));
        cs.setWrapText(true);
        return cs;
    }
}
