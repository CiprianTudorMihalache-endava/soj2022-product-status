package com.endava.tmd.soj.ps.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class EmagContentReaderServiceTest {
    private final EmagContentReaderService service = new EmagContentReaderService();

    @Test
    void readCurrentPriceForProductAvailable() throws IOException {
        final var content = Files.readString(Path.of("src/test/resources/emagProductAvailable.htm"));
        final var crtStatus = service.readCurrentStatus(content);
        assertThat(crtStatus.getTitle())
                .isEqualTo("Telefon mobil Samsung Galaxy S21 Ultra, Dual SIM, 128GB, 12GB RAM, 5G, Phantom Black");
        assertThat(crtStatus.getAvailabilityLabel()).isEqualTo("\u00cen stoc");
        assertThat(crtStatus.getPrice()).isEqualTo(5218.83, offset(1e-10));
        assertThat(crtStatus.isAvailable()).isTrue();
    }

    @Test
    void readCurrentPriceForProductLimitedAvailability() throws IOException {
        final var content = Files.readString(Path.of("src/test/resources/emagProductLimitedAvailability.htm"));
        final var crtStatus = service.readCurrentStatus(content);
        assertThat(crtStatus.getTitle())
                .isEqualTo("Telefon mobil Samsung Galaxy S21 Ultra, Dual SIM, 256GB, 12GB RAM, 5G, Phantom Silver");
        assertThat(crtStatus.getAvailabilityLabel()).isEqualTo("Ultimul produs in stoc");
        assertThat(crtStatus.getPrice()).isEqualTo(6399, offset(1e-10));
        assertThat(crtStatus.isAvailable()).isTrue();
    }

    @Test
    void readCurrentPriceForProductLimitedAvailability2() throws IOException {
        final var content = Files.readString(Path.of("src/test/resources/emagProductLimitedAvailability2.htm"));
        final var crtStatus = service.readCurrentStatus(content);
        assertThat(crtStatus.getTitle())
                .isEqualTo("Telefon mobil Samsung Galaxy A32, Dual SIM, 128GB, 4G, Black");
        assertThat(crtStatus.getAvailabilityLabel()).isEqualTo("Ultimele 3 produse");
        assertThat(crtStatus.getPrice()).isEqualTo(1269.98, offset(1e-10));
        assertThat(crtStatus.isAvailable()).isTrue();
    }

    @Test
    void readCurrentPriceForProductOutOfStock() throws IOException {
        final var content = Files.readString(Path.of("src/test/resources/emagProductOutOfStock.htm"));
        final var crtStatus = service.readCurrentStatus(content);
        assertThat(crtStatus.getTitle())
                .isEqualTo("Telefon mobil Samsung Galaxy S22 Ultra, Dual SIM, 256GB, 12GB RAM, 5G, Phantom White");
        assertThat(crtStatus.getAvailabilityLabel()).isEqualTo("Stoc epuizat");
        assertThat(crtStatus.getPrice()).isEqualTo(6699.98, offset(1e-10));
        assertThat(crtStatus.isAvailable()).isFalse();
    }

    @Test
    void readCurrentPriceForProductPreorder() throws IOException {
        final var content = Files.readString(Path.of("src/test/resources/emagProductPreorder.htm"));
        final var crtStatus = service.readCurrentStatus(content);
        assertThat(crtStatus.getTitle())
                .isEqualTo("Telefon mobil Samsung Galaxy S22 Ultra, Dual SIM, 512GB, 12GB RAM, 5G, Phantom Black");
        assertThat(crtStatus.getAvailabilityLabel()).isEqualTo("Precomand\u0103");
        assertThat(crtStatus.getPrice()).isEqualTo(7199.98, offset(1e-10));
        assertThat(crtStatus.isAvailable()).isTrue();
    }

    @Test
    void readCurrentPriceForProductShowroomOnlyAvailability() throws IOException {
        final var content = Files.readString(Path.of("src/test/resources/emagProductShowroomOnlyAvailability.htm"));
        final var crtStatus = service.readCurrentStatus(content);
        assertThat(crtStatus.getTitle())
                .isEqualTo("Telefon mobil Samsung Galaxy S22 Ultra, Dual SIM, 256GB, 12GB RAM, 5G, Phantom White");
        assertThat(crtStatus.getAvailabilityLabel()).isEqualTo("disponibil in Showroom");
        assertThat(crtStatus.getPrice()).isEqualTo(6699.98, offset(1e-10));
        assertThat(crtStatus.isAvailable()).isTrue();
    }

    @Test
    void readCurrentPriceForProductUnavailable() throws IOException {
        final var content = Files.readString(Path.of("src/test/resources/emagProductUnavailable.htm"));
        final var crtStatus = service.readCurrentStatus(content);
        assertThat(crtStatus.getTitle())
                .isEqualTo("Telefon mobil Samsung Galaxy Note 10 Plus 5G, Dual SIM, 256GB, 12GB RAM, Aura Black");
        assertThat(crtStatus.getAvailabilityLabel()).isEqualTo("Indisponibil");
        assertThat(crtStatus.getPrice()).isEqualTo(5999, offset(1e-10));
        assertThat(crtStatus.isAvailable()).isFalse();
    }

    // of course we should write unit tests also for cases when an exception could be thrown
}
