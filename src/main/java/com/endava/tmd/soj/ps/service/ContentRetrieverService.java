package com.endava.tmd.soj.ps.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

import com.endava.tmd.soj.ps.model.ProductCurrentStatus;

// https://www.baeldung.com/java-http-request
public class ContentRetrieverService {
    private final ContentReader contentReader;

    public ContentRetrieverService(final ContentReader contentReader) {
        this.contentReader = contentReader;
    }

    public ProductCurrentStatus readCurrentStatus(final String productAddress) {
        final var crtContent = readCurrentContent(productAddress);
        return crtContent.map(contentReader::readCurrentStatus).orElse(createInexistentProduct());
    }

    private Optional<String> readCurrentContent(final String productAddress) {
        try {
            final var url = new URL(productAddress);
            final var con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000); // this could be part of UserPreferences
            con.setReadTimeout(5000); // the same
            final var responseCode = con.getResponseCode();
            if (responseCode == 200) {
                // https://www.baeldung.com/convert-input-stream-to-string
                return Optional.of(new String(con.getInputStream().readAllBytes()));
            }
        } catch (final IOException e) {
            // we cannot retrieve the product, no problem
        }
        return Optional.empty();
    }

    private ProductCurrentStatus createInexistentProduct() {
        return new ProductCurrentStatus()
                .setTitle("Pagina nu poate fi accesata")
                .setAvailable(false)
                .setAvailabilityLabel("Inexistent");
    }

}
