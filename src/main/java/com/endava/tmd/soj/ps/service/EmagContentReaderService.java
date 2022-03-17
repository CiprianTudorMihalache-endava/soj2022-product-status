package com.endava.tmd.soj.ps.service;

import com.endava.tmd.soj.ps.exception.UnableToReadCurrentProductStatusException;
import com.endava.tmd.soj.ps.model.ProductCurrentStatus;
import com.endava.tmd.soj.ps.model.emag.Availability;
import com.endava.tmd.soj.ps.model.emag.Flags;
import com.endava.tmd.soj.ps.model.emag.Price;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EmagContentReaderService implements ContentReader {
    private static final String EM_OFFER = "EM.offer = {";
    private static final String EM_PRODUCT_TITLE = "EM.product_title = \"";
    private static final String IN = " in: ";

    // https://www.baeldung.com/jackson-deserialize-json-unknown-properties
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public ProductCurrentStatus readCurrentStatus(final String webPageContent) {
        return new ProductCurrentStatus()
                .setTitle(readProductTitle(webPageContent))
                .setAvailabilityLabel(readAvailability(webPageContent).getText())
                .setPrice(readPrice(webPageContent).getCurrent())
                .setAvailable(readFlags(webPageContent).isAvailableForOrder());
    }

    private String readProductTitle(final String webPageContent) {
        final var startIndex = webPageContent.indexOf(EM_PRODUCT_TITLE);
        if (startIndex < 0) {
            throw new UnableToReadCurrentProductStatusException("Cannot find product title start index" + IN + webPageContent);
        }

        final var endIndex = webPageContent.indexOf("\";", startIndex);
        if (endIndex < 0) {
            throw new UnableToReadCurrentProductStatusException("Cannot find product title end index" + IN + webPageContent);
        }

        return webPageContent.substring(startIndex + EM_PRODUCT_TITLE.length(), endIndex);
    }

    private Availability readAvailability(final String webPageContent) {
        final var availabilityJson = readJson(webPageContent, EM_OFFER, "availability: {");
        try {
            // https://mkyong.com/java/jackson-how-to-parse-json/
            return objectMapper.readValue(availabilityJson, Availability.class);
        } catch (final JsonProcessingException e) {
            throw new UnableToReadCurrentProductStatusException(e);
        }
    }

    private Price readPrice(final String webPageContent) {
        final var priceJson = readJson(webPageContent, EM_OFFER, "price: {");
        try {
            return objectMapper.readValue(priceJson, Price.class);
        } catch (final JsonProcessingException e) {
            throw new UnableToReadCurrentProductStatusException(e);
        }
    }

    private Flags readFlags(final String webPageContent) {
        final var flagsJson = readJson(webPageContent, EM_OFFER, "flags: {");
        try {
            return objectMapper.readValue(flagsJson, Flags.class);
        } catch (final JsonProcessingException e) {
            throw new UnableToReadCurrentProductStatusException(e);
        }
    }

    private String readJson(final String webPageContent, final String regionMarker, final String attribute) {
        final var regionMarkerIndex = webPageContent.indexOf(regionMarker);
        if (regionMarkerIndex < 0) {
            throw new UnableToReadCurrentProductStatusException("Cannot find '" + regionMarker + "'" + IN + webPageContent);
        }

        final var attributeIndex = webPageContent.indexOf(attribute, regionMarkerIndex);
        if (attributeIndex < 0) {
            throw new UnableToReadCurrentProductStatusException("Cannot find '" + attribute + "' starting with index " + regionMarkerIndex
                    + IN + webPageContent);
        }

        return readJson(webPageContent, attributeIndex + attribute.length() - 1);

    }

    private String readJson(final String webPageContent, final int index) {
        if (webPageContent.charAt(index) != '{') {
            throw new UnableToReadCurrentProductStatusException("Expected to find '{' on index " + index + IN + webPageContent);
        }

        var openNb = 1;
        var increaseIndex = 0;

        for (var i = index + 1; i < webPageContent.length(); i++) {
            increaseIndex++;
            if (webPageContent.charAt(i) == '{') {
                openNb++;
            }
            if (webPageContent.charAt(i) == '}') {
                openNb--;
                if (openNb == 0) {
                    return webPageContent.substring(index, index + increaseIndex + 1);
                }
            }
        }

        throw new UnableToReadCurrentProductStatusException(
                "Unable to find the matching '}' for the one that opens on index " + index + IN + webPageContent);
    }
}
