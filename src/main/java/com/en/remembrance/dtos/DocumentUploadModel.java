package com.en.remembrance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentUploadModel {

    private MultipartFile document;

    @NotEmpty(message = "Name cannot be null")
    @NotNull(message = "Name cannot be null")
    private String name;

    private String description;

}
