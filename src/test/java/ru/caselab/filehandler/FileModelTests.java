package ru.caselab.filehandler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.caselab.filehandler.model.File;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FileModelTests {
    @Test
    @DisplayName("Create file model")
    public void testFileCreation() {
        Date now = new Date();
        File file = new File();
        file.setTitle("Test Title");
        file.setCreationDate(now);
        file.setDescription("Test Description");
        file.setData("TestData");

        assertThat(file).hasFieldOrPropertyWithValue("title", "Test Title");
        assertThat(file).hasFieldOrPropertyWithValue("creationDate", now);
        assertThat(file).hasFieldOrPropertyWithValue("description", "Test Description");
        assertThat(file).hasFieldOrPropertyWithValue("data", "TestData");
    }
}
