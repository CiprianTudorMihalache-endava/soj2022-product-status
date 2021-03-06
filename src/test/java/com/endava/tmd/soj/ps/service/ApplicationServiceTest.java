package com.endava.tmd.soj.ps.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.endava.tmd.soj.ps.model.Data;
import com.endava.tmd.soj.ps.model.ProductCurrentStatus;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private UserPreferencesLoaderService userPrefLoaderService;

    @Mock
    private ContentRetrieverService contentRetrieverService;

    @Mock
    private ExcelService excelService;

    @InjectMocks
    private ApplicationService service;

    @Captor
    private ArgumentCaptor<Data> dataCaptor;

    @Captor
    private ArgumentCaptor<File> fileCaptor;

    @Test
    void noExistingReportFile() {
        // Arrange
        final var inputFile = new File("src/test/resources/input1Product.yaml");
        when(userPrefLoaderService.load(inputFile)).thenCallRealMethod();
        when(contentRetrieverService.readCurrentStatus("a")).thenReturn(new ProductCurrentStatus()
                .setPrice(2.43)
                .setAvailable(true)
                .setAvailabilityLabel("Ultimele 2 produse")
                .setTitle("A-super product"));

        // Act
        service.execute(inputFile);

        // Assert
        verify(excelService).saveData(dataCaptor.capture(), fileCaptor.capture());

        final var exportedData = dataCaptor.getValue();
        assertThat(exportedData.getHeaders()).singleElement().satisfies(header -> {
            final var parsedHeader = LocalDateTime.parse(header, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            assertThat(parsedHeader).isCloseTo(LocalDateTime.now(), within(3, ChronoUnit.SECONDS));
        });
        assertThat(exportedData.getProducts()).hasSize(1);
        // another approach would be to have a mother building the product
        // in this case we do not need to test each object attribute,
        // we would simply assert that it has all attributes equal to the object generated by its mother
        assertThat(exportedData.getProducts().get("a")).satisfies(productHistory -> {
            assertThat(productHistory.getTitle()).isEqualTo("A-super product");
            assertThat(productHistory.getStatusHistory()).singleElement().satisfies(historicalStatus -> {
                assertThat(historicalStatus.getPrice()).isEqualTo(2.43, byLessThan(1e-10));
                assertThat(historicalStatus.isAvailable()).isTrue();
                assertThat(historicalStatus.getAvailabilityLabel()).isEqualTo("Ultimele 2 produse");
            });
        });

        assertThat(fileCaptor.getValue()).hasName("report.xlsx");

        verifyNoMoreInteractions(userPrefLoaderService, contentRetrieverService, excelService);
    }

    // we need to add tests for at least the following scenarios:
    // * existingReportFileNoNewProductNoDeletedProduct
    // * existingReportFileOneNewProductNoDeletedProduct
    // * existingReportFileNoNewProductOneDeletedProduct
    // * existingReportFileOneNewProductOneDeletedProduct
}
