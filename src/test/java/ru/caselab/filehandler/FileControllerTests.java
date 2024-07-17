package ru.caselab.filehandler;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.caselab.filehandler.controller.FileController;
import ru.caselab.filehandler.dto.CreateFileRequest;
import ru.caselab.filehandler.dto.FileResponse;
import ru.caselab.filehandler.dto.PageFilesResponse;
import ru.caselab.filehandler.mapper.FileMapper;
import ru.caselab.filehandler.model.File;
import ru.caselab.filehandler.service.FileService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class FileControllerTests {
    @MockBean
    private FileService fileService;

    @MockBean
    private FileMapper fileMapper;

    @Autowired
    private FileController fileController;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("File creation")
    void testCreateFile() {
        CreateFileRequest request = new CreateFileRequest();
        request.setTitle("Test Title");
        request.setCreationDate(new Date());
        request.setDescription("Test Description");
        request.setData("Test Data");

        when(fileMapper.toEntity(any(CreateFileRequest.class))).thenReturn(new File());
        when(fileService.save(any(File.class))).thenReturn(1L);

        ResponseEntity<Long> response = fileController.createFile(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Get file by id")
    void testGetFileById() {
        File file = new File();
        file.setTitle("Test Title");
        file.setCreationDate(new Date());
        file.setDescription("Test Description");
        file.setData("Test Data");

        when(fileService.findById(1L)).thenReturn(file);
        when(fileMapper.toDto(any(File.class))).thenReturn(new FileResponse());

        ResponseEntity<FileResponse> response = fileController.getFileById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Get info on page in correct pages segment")
    void testGetAllByPageAndLimit() {
        List<File> files = new ArrayList<>();
        files.add(new File());
        files.add(new File());

        PageFilesResponse resp = new PageFilesResponse();
        resp.setTotalPages(1);
        resp.setCurrentPage(1);

        Page<File> page = new PageImpl<>(files);

        when(fileService.findPage(any(PageRequest.class))).thenReturn(page);
        when(fileMapper.toDtos(any(Page.class))).thenReturn(resp);

        ResponseEntity<PageFilesResponse> response = fileController.getAllByPageAndLimit(1, 5, Sort.Direction.DESC);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Get info on page in wrong pages segment")
    void testGetAllByPageAndLimitRedirect() {
        List<File> files = new ArrayList<>();
        files.add(new File());
        files.add(new File());

        PageFilesResponse resp = new PageFilesResponse();
        resp.setTotalPages(1);
        resp.setCurrentPage(1);

        Page<File> page = new PageImpl<>(files);

        when(fileService.findPage(any(PageRequest.class))).thenReturn(page);
        when(fileMapper.toDtos(any(Page.class))).thenReturn(resp);

        ResponseEntity<PageFilesResponse> response = fileController.getAllByPageAndLimit(2, 5, Sort.Direction.DESC);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody()).isNotNull();
    }
}
