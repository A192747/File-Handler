package ru.caselab.filehandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.caselab.filehandler.model.File;
import ru.caselab.filehandler.repository.FileRepository;
import ru.caselab.filehandler.service.FileService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FileServiceTests {
    @MockBean
    private FileRepository fileRepository;

    @Autowired
    private FileService fileService;

    private File file;
    @BeforeEach
    void setUp() {
        file = new File();
        file.setId(1L);
        file.setTitle("Test File");
        file.setCreationDate(new Date());
        file.setDescription("This is a test file");
        file.setData("Test data");

    }

    @Test
    @DisplayName("Save new file")
    void save() {
        when(fileRepository.save(file)).thenReturn(file);

        Long savedFileId = fileService.save(file);

        assertEquals(file.getId(), savedFileId);
        verify(fileRepository, times(1)).save(file);
    }

    @Test
    @DisplayName("Find by id")
    void findById() {
        when(fileRepository.findById(file.getId())).thenReturn(Optional.of(file));

        File foundFile = fileService.findById(file.getId());

        assertEquals(file, foundFile);
        verify(fileRepository, times(1)).findById(file.getId());
    }

    @Test
    @DisplayName("Find by id, but not found")
    void findByIdNotFound() {
        when(fileRepository.findById(file.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> fileService.findById(file.getId()));
        verify(fileRepository, times(1)).findById(file.getId());
    }

    @Test
    @DisplayName("Find page with no files")
    void findPageWithNoFiles() {
        List<File> files = new ArrayList<>();
        Page<File> page = new PageImpl<>(files);
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(fileRepository.findAll(pageRequest)).thenReturn(page);

        assertThrows(NoSuchElementException.class, () -> fileService.findPage(pageRequest));
        verify(fileRepository, times(1)).findAll(pageRequest);
    }

    @Test
    @DisplayName("Find page")
    void findPage() {
        List<File> files = new ArrayList<>();
        files.add(file);
        Page<File> page = new PageImpl<>(files);
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(fileRepository.findAll(pageRequest)).thenReturn(page);

        Page<File> foundPage = fileService.findPage(pageRequest);

        assertEquals(page, foundPage);
        verify(fileRepository, times(1)).findAll(pageRequest);
    }


    @Test
    @DisplayName("Page not found")
    void findPageButNotFound() {
        List<File> files = new ArrayList<>();
        Page<File> page = new PageImpl<>(files);
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(fileRepository.findAll(pageRequest)).thenReturn(page);

        Page<File> foundPage = fileService.findPage(pageRequest);

        assertEquals(page, foundPage);
        verify(fileRepository, times(1)).findAll(pageRequest);
    }
}
