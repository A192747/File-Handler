package ru.caselab.filehandler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.caselab.filehandler.model.File;
import ru.caselab.filehandler.repository.FileRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    @Transactional
    public Long save(File file) {
        return fileRepository.save(file).getId();
    }

    public File findById(Long fileId) {
        return fileRepository.findById(fileId).orElseThrow(() -> new NoSuchElementException("Не удалось найти файл с таким id"));
    }

    public Page<File> findPage(PageRequest request) {
        Page<File> page = fileRepository.findAll(request);
        if (page.getTotalElements() == 0) {
            throw new NoSuchElementException("На данный момент нет сохраненных файлов");
        }
        return fileRepository.findAll(request);
    }
}
