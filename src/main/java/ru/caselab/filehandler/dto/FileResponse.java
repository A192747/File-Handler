package ru.caselab.filehandler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
public class FileResponse {
    @NotBlank
    @JsonProperty("title")
    private String title;
    @NonNull
    @JsonProperty("creation_date")
    private Date creationDate;
    @NotBlank
    @JsonProperty("description")
    private String description;
    @NotBlank
    @JsonProperty("file")
    private String data;
}
