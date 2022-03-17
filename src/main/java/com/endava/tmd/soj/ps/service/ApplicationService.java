package com.endava.tmd.soj.ps.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.endava.tmd.soj.ps.model.Data;
import com.endava.tmd.soj.ps.model.ProductCurrentStatus;
import com.endava.tmd.soj.ps.model.ProductHistoricalStatus;
import com.endava.tmd.soj.ps.model.ProductHistory;

public class ApplicationService {
    private static final String EXCEL_FILE_EXTENSION = ".xlsx";

    private final UserPreferencesLoaderService userPrefLoaderService;
    private final ContentRetrieverService contentRetrieverService;
    private final ExcelService excelService;

    public ApplicationService(final UserPreferencesLoaderService userPrefLoaderService,
                              final ContentRetrieverService contentRetrieverService,
                              final ExcelService excelService) {
        this.userPrefLoaderService = userPrefLoaderService;
        this.contentRetrieverService = contentRetrieverService;
        this.excelService = excelService;
    }

    public void execute(final File userPreferencesFile) {
        final var userPreferences = userPrefLoaderService.load(userPreferencesFile);
        final var excelFile = new File(userPreferences.getReportFileName() + EXCEL_FILE_EXTENSION);
        final var data = readExistingData(excelFile);
        removeProductsThatAreNotWantedAnymore(data, userPreferences.getProducts());
        removeUnnecessaryHeaders(data);
        addCurrentDateToHeaders(data);
        readCurrentProductsStatus(data, userPreferences.getProducts());
        removeOldReportFile(excelFile, userPreferences.isDeleteOldReport());
        excelService.saveData(data, excelFile);
    }

    private Data readExistingData(final File excelFile) {
        if (!excelFile.exists()) {
            return new Data();
        }

        return excelService.readExistingData(excelFile);
    }

    private void addCurrentDateToHeaders(final Data data) {
        final var currentDate = LocalDateTime.now();
        // https://howtodoinjava.com/java/date-time/format-localdatetime-to-string/
        final var currentDateString = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        data.getHeaders().add(0, currentDateString);
    }

    private void removeProductsThatAreNotWantedAnymore(final Data data, final List<String> currentProducts) {
        data.getProducts().entrySet().removeIf(e -> !currentProducts.contains(e.getKey()));
    }

    private void readCurrentProductsStatus(final Data data, final List<String> currentProducts) {
        for (final String productUrl : currentProducts) {
            final var crtStatus = contentRetrieverService.readCurrentStatus(productUrl);
            var productHistory = data.getProducts().get(productUrl);
            if (productHistory == null) {
                productHistory = new ProductHistory();
                data.getProducts().put(productUrl, productHistory);
            }

            productHistory.getStatusHistory().add(0, buildHistoricalStatus(crtStatus));
            productHistory.setTitle(crtStatus.getTitle());
        }
    }

    private ProductHistoricalStatus buildHistoricalStatus(final ProductCurrentStatus crtStatus) {
        return new ProductHistoricalStatus()
                .setAvailabilityLabel(crtStatus.getAvailabilityLabel())
                .setAvailable(crtStatus.isAvailable())
                .setPrice(crtStatus.getPrice());
    }

    @SuppressWarnings("java:S5413")
    private void removeUnnecessaryHeaders(final Data data) {
        final var headers = data.getHeaders();
        final var maxHistoryLength = findMaxHistoryLength(data);
        final var nbElementsToBeRemoved = headers.size() - maxHistoryLength;
        for (var i = 0; i < nbElementsToBeRemoved; i++) {
            headers.remove(headers.size() - 1);
        }

    }

    private int findMaxHistoryLength(final Data data) {
        return data.getProducts().values().stream()
                .map(productHistory -> productHistory.getStatusHistory().size())
                .max(Integer::compare)
                .orElse(0);
    }

    private void removeOldReportFile(final File excelFile, final boolean deleteOldReport) {
        if (!excelFile.exists()) {
            return;
        }

        final var path = excelFile.toPath();
        if (!deleteOldReport) {
            moveInputFile(excelFile, path);
        } else {
            deleteInputFile(path);
        }
    }

    private void moveInputFile(final File excelFile, final Path path) {
        final var newPath = path.resolveSibling(getNewFileName(excelFile.getName()));
        try {
            Files.move(path, newPath);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String getNewFileName(final String fileName) {
        if (!fileName.endsWith(EXCEL_FILE_EXTENSION)) {
            throw new IllegalStateException(
                    "Expected the existing report name <" + fileName + "> to have extension " + EXCEL_FILE_EXTENSION);
        }
        return fileName.substring(0, fileName.length() - EXCEL_FILE_EXTENSION.length()) + "_" + System.currentTimeMillis()
                + EXCEL_FILE_EXTENSION;
    }

    private void deleteInputFile(final Path path) {
        try {
            Files.delete(path);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
