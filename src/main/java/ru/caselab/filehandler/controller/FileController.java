package ru.caselab.filehandler.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.caselab.filehandler.dto.CreateFileRequest;
import ru.caselab.filehandler.dto.FileResponse;
import ru.caselab.filehandler.dto.PageFilesResponse;
import ru.caselab.filehandler.mapper.FileMapper;
import ru.caselab.filehandler.service.FileService;

@RestController
@Validated
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    @Value("${sorting.field:creationDate}")
    private String field;

    private final FileService fileService;
    private final FileMapper fileMapper;

    @SneakyThrows
    @PostMapping
    public ResponseEntity<Long> createFile(@Valid @RequestBody CreateFileRequest request) {
        Long fileId = fileService.save(fileMapper.toEntity(request));
        return ResponseEntity.ok(fileId);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<FileResponse> getFileById(@PathVariable @NonNull @Min(value = 1) Long fileId) {
        return ResponseEntity.ok(fileMapper.toDto(fileService.findById(fileId)));
    }

    //Доп задание с пагинацией
    @GetMapping()
    public ResponseEntity<PageFilesResponse> getAllByPageAndLimit(
            @RequestParam(name = "page", required = false, defaultValue = "1") @Min(value = 1) Integer page,
            @RequestParam(name = "limit", required = false, defaultValue = "5") @Min(value = 1) @Max(value = 100) Integer limit,
            @RequestParam(name = "sortDirection", required = false, defaultValue = "DESC") Sort.Direction sortDirection) {

        Sort sort = Sort.by(sortDirection, field);

        PageFilesResponse response = fileMapper.toDtos(
                fileService.findPage(PageRequest.of(page - 1, limit, sort))
        );
        if (page > response.getTotalPages() && response.getTotalPages() > 0) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(ServletUriComponentsBuilder.fromCurrentRequest()
                            .replaceQueryParam("page", response.getTotalPages())
                            .build().toUri())
                    .body(response);
        }
        return ResponseEntity.ok(response);
    }
}
