package ru.caselab.filehandler.mapper;

import org.springframework.data.domain.Page;
import ru.caselab.filehandler.dto.CreateFileRequest;
import ru.caselab.filehandler.dto.FileResponse;
import ru.caselab.filehandler.dto.PageFilesResponse;
import ru.caselab.filehandler.model.File;

public interface FileMapper {
    File toEntity(CreateFileRequest request);

    FileResponse toDto(File file);

    PageFilesResponse toDtos(Page<File> files);
}
