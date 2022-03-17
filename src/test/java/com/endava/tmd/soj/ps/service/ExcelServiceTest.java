package com.endava.tmd.soj.ps.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.endava.tmd.soj.ps.test.mothers.DataMother;

class ExcelServiceTest {
    private ExcelService service = new ExcelService();

    @Test
    void importShouldCorrectlyReadTheExportedData() throws IOException {
        final var excelFile = File.createTempFile("report", ".xlsx", new File("target"));
        excelFile.deleteOnExit();

        final var initialData = DataMother.phones3Times();
        service.saveData(initialData, excelFile);

        final var importedData = service.readExistingData(excelFile);
        assertThat(importedData)
                .usingRecursiveComparison()
                .isEqualTo(initialData);
    }

}
