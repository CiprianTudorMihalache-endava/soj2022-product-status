package com.endava.tmd.soj.ps.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.endava.tmd.soj.ps.exception.IncompleteUserPreferencesException;
import com.endava.tmd.soj.ps.exception.UnableToReadUserPreferencesException;
import com.endava.tmd.soj.ps.model.UserPreferences;

class UserPreferencesLoaderServiceTest {
    private final UserPreferencesLoaderService service = new UserPreferencesLoaderService();

    @Test
    void shouldLoadUserPreferencesFromAValidFile() {
        final var userPreferencesFile = new File("src/test/resources/input.yaml");
        final var userPreferences = service.load(userPreferencesFile);
        assertThat(userPreferences.getReportFileName()).isEqualTo("report");
        assertThat(userPreferences.isDeleteOldReport()).isTrue();
        assertThat(userPreferences.getProducts()).containsExactly("a", "b", "c");
    }

    @Test
    void shouldThrowExceptionWhenTheFileDoesNotExist() {
        final var userPreferencesFile = new File("doesNotExist.yaml");
        assertThatThrownBy(() -> service.load(userPreferencesFile))
                .isInstanceOf(UnableToReadUserPreferencesException.class)
                .hasMessageContaining("The system cannot find the file specified");
    }

    @Test
    void shouldThrowExceptionWhenTheFileDoesNotContainTheCorrectData() {
        final var userPreferencesFile = new File("pom.xml");
        assertThatThrownBy(() -> service.load(userPreferencesFile))
                .isInstanceOf(UnableToReadUserPreferencesException.class)
                .hasMessageContaining("Cannot construct instance of `" + UserPreferences.class.getName() + "`");
    }

    @Test
    void shouldThrowExceptionWhenTheFileDoesNotContainTheReportFileName() {
        final var userPreferencesFile = new File("src/test/resources/inputNoReportFileName.yaml");
        assertThatThrownBy(() -> service.load(userPreferencesFile))
                .isInstanceOf(IncompleteUserPreferencesException.class)
                .hasMessageContaining("There is no valid report file name specified in the input file");
    }

    @Test
    void shouldThrowExceptionWhenTheFileDoesNotContainAnyProduct() {
        final var userPreferencesFile = new File("src/test/resources/inputNoProducts.yaml");
        assertThatThrownBy(() -> service.load(userPreferencesFile))
                .isInstanceOf(IncompleteUserPreferencesException.class)
                .hasMessageContaining("There is no product specified in the input file");
    }

}
