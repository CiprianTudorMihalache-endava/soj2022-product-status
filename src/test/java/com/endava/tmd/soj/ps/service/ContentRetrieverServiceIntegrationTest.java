package com.endava.tmd.soj.ps.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

class ContentRetrieverServiceIntegrationTest {

    private final ContentRetrieverService service = new ContentRetrieverService(new EmagContentReaderService());

    private MockWebServer mockWebServer;
    private String productUrl;

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        productUrl = "http://localhost:" + mockWebServer.getPort() + "/myProduct";
    }

    @AfterEach
    void cleanup() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void shouldReturnProperStatusForSuccessfulHttpRetrieval() throws IOException {
        // Arrange
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "text/html; charset=UTF-8")
                .setBody(Files.readString(Path.of("src/test/resources/emagProductAvailable.htm"))));

        // Act
        final var crtStatus = service.readCurrentStatus(productUrl);

        // Assert
        assertThat(crtStatus.getTitle())
                .isEqualTo("Telefon mobil Samsung Galaxy S21 Ultra, Dual SIM, 128GB, 12GB RAM, 5G, Phantom Black");
        assertThat(crtStatus.getAvailabilityLabel()).isEqualTo("\u00cen stoc");
        assertThat(crtStatus.getPrice()).isEqualTo(5218.83, offset(1e-10));
        assertThat(crtStatus.isAvailable()).isTrue();
    }

    @Test
    void shouldReturnAValidStatusWhenHttpEndpointIsNotAvailable() throws IOException {
        // Arrange
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "text/html; charset=UTF-8")
                .setResponseCode(404)
                .setBody("The page you searched cannot be found"));

        // Act
        final var crtStatus = service.readCurrentStatus(productUrl);

        // Assert
        assertThat(crtStatus.getTitle())
                .isEqualTo("Pagina nu poate fi accesata");
        assertThat(crtStatus.getAvailabilityLabel()).isEqualTo("Inexistent");
        assertThat(crtStatus.getPrice()).isEqualTo(0, offset(1e-10));
        assertThat(crtStatus.isAvailable()).isFalse();
    }
}
