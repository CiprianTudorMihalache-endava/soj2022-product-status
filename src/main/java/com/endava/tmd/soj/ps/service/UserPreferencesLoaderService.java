package com.endava.tmd.soj.ps.service;

import java.io.File;
import java.io.IOException;

import com.endava.tmd.soj.ps.exception.IncompleteUserPreferencesException;
import com.endava.tmd.soj.ps.exception.UnableToReadUserPreferencesException;
import com.endava.tmd.soj.ps.model.UserPreferences;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

// https://stackabuse.com/reading-and-writing-yaml-files-in-java-with-jackson/
public class UserPreferencesLoaderService {

    /**
     * Loads the user preferences
     * 
     * @param userPreferencesFile The yaml file containing the user preferences
     * @return
     * @throws UnableToReadUserPreferencesException In case something goes wrong with the read operation
     */
    public UserPreferences load(final File userPreferencesFile) {
        try {
            final var userPreferences = new ObjectMapper(new YAMLFactory())
                    .readValue(userPreferencesFile, UserPreferences.class);
            validate(userPreferences);
            return userPreferences;
        } catch (final IOException e) {
            throw new UnableToReadUserPreferencesException(e);
        }
    }

    private void validate(final UserPreferences userPreferences) {
        validateReportFileName(userPreferences);
        validateProducts(userPreferences);
    }

    private void validateReportFileName(final UserPreferences userPreferences) {
        final var reportFileName = userPreferences.getReportFileName();
        if (reportFileName == null || reportFileName.isBlank()) {
            throw new IncompleteUserPreferencesException("There is no valid report file name specified in the input file");
        }
    }

    private void validateProducts(final UserPreferences userPreferences) {
        final var products = userPreferences.getProducts();
        if (products == null || products.isEmpty()) {
            throw new IncompleteUserPreferencesException("There is no product specified in the input file");
        }
    }

}
