package ru.caselab.filehandler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.caselab.filehandler.dto.CreateFileRequest;
import ru.caselab.filehandler.dto.FileResponse;
import ru.caselab.filehandler.dto.PageFilesResponse;
import ru.caselab.filehandler.model.File;
import ru.caselab.filehandler.mapper.FileMapper;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FileMapperTests {
    @Autowired
    private FileMapper fileMapper;

    @Test
    @DisplayName("Process dto to Entity")
    void testToEntity() {
        CreateFileRequest request = new CreateFileRequest();
        request.setTitle("Test Title");
        request.setCreationDate(java.util.Date.from(Instant.EPOCH));
        request.setDescription("Test Description");
        request.setData("Test Data");

        File file = fileMapper.toEntity(request);

        assertThat(file.getTitle()).isEqualTo(request.getTitle());
        assertThat(file.getCreationDate()).isEqualTo(request.getCreationDate());
        assertThat(file.getDescription()).isEqualTo(request.getDescription());
        assertThat(file.getData()).isEqualTo(request.getData());
    }

    @Test
    @DisplayName("Process file to Dto")
    void testToDto() {
        File file = new File();
        file.setTitle("Test Title");
        file.setCreationDate(java.util.Date.from(Instant.EPOCH));
        file.setDescription("Test Description");
        file.setData("Test Data");

        FileResponse response = fileMapper.toDto(file);

        assertThat(response.getTitle()).isEqualTo(file.getTitle());
        assertThat(response.getCreationDate()).isEqualTo(file.getCreationDate());
        assertThat(response.getDescription()).isEqualTo(file.getDescription());
        assertThat(response.getData()).isEqualTo(file.getData());
    }

    @Test
    @DisplayName("Process correct files to Dtos")
    void testToDtos() {
        List<File> list = new ArrayList<>();
        java.util.Date now = new Date();
        File file = new File();
        file.setTitle("Test Title");
        file.setCreationDate(now);
        file.setDescription("Test Description");
        file.setData("TestData");

        list.add(file);
        Page<File> files = new PageImpl<>(list);

        PageFilesResponse responses = fileMapper.toDtos(files);


        assertThat(responses.getFiles().size()).isEqualTo(1);
        assertThat(responses.getTotalFiles()).isEqualTo(1);
    }

    @Test
    @DisplayName("Process wrong files to Dtos")
    void testToDtosPageWrongFiles() {
        List<File> files = new ArrayList<>();
        files.add(new File());
        files.add(new File());

        Page<File> page = new PageImpl<>(files);



        Assertions.assertThrows(NullPointerException.class, () -> fileMapper.toDtos(page));

//        assertThat(response.getTotalFiles()).isEqualTo(2);
//        assertThat(response.getTotalPages()).isEqualTo(1);
//        assertThat(response.getCurrentPage()).isEqualTo(1);
//        assertThat(response.getFiles().size()).isEqualTo(2);
    }
}