package com.en.remembrance.dtos;

import com.en.remembrance.domain.TitlePage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TitlePageModel {

    private Long id;

    @Min(value = 1L, message = "Please select a Genre")
    private Long categoryId;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Author cannot be empty")
    private String author;

    private MultipartFile image;

    private String imageType;

    private String base64Image;

    public TitlePageModel(TitlePage titlePage) {
        this.id = titlePage.getId();
        this.title = titlePage.getTitle();
        this.author = titlePage.getAuthor();
        this.base64Image = titlePage.getBase64Image();
    }

}
