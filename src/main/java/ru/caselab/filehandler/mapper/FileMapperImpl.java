package ru.caselab.filehandler.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.caselab.filehandler.dto.CreateFileRequest;
import ru.caselab.filehandler.dto.FileResponse;
import ru.caselab.filehandler.dto.PageFilesResponse;
import ru.caselab.filehandler.model.File;

import java.util.List;

@Component
public class FileMapperImpl implements FileMapper {
    @Override
    public File toEntity(CreateFileRequest request) {
        File file = new File();
        file.setTitle(request.getTitle());
        file.setCreationDate(request.getCreationDate());
        file.setDescription(request.getDescription());
        file.setData(request.getData());
        return file;
    }

    @Override
    public FileResponse toDto(File file) {
        FileResponse response = new FileResponse();
        response.setTitle(file.getTitle());
        response.setCreationDate(file.getCreationDate());
        response.setDescription(file.getDescription());
        response.setData(file.getData());
        return response;
    }

    private List<FileResponse> toDtos(List<File> files) {
        return files.stream().map(this::toDto).toList();
    }

    @Override
    public PageFilesResponse toDtos(Page<File> files) {
        PageFilesResponse pageFilesResponse = new PageFilesResponse();
        pageFilesResponse.setTotalFiles(files.getTotalElements());
        pageFilesResponse.setTotalPages(files.getTotalPages());
        pageFilesResponse.setCurrentPage(files.getNumber() + 1);
        pageFilesResponse.setFiles(toDtos(files.getContent()));
        return pageFilesResponse;
    }
}