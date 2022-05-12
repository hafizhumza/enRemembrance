package com.en.remembrance.dtos;

import com.en.remembrance.domain.ConclusionPage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConclusionPageModel {

    private Long id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    private String author;

    private int hasText;

    private String text;

    private MultipartFile image;

    private String imageType;

    private String base64Image;

    public ConclusionPageModel(ConclusionPage conclusionPage) {
        this.id = conclusionPage.getId();
        this.title = conclusionPage.getTitle();
        this.author = conclusionPage.getAuthor();
        this.hasText = conclusionPage.getHasText() ? 1 : 0;
        this.text = conclusionPage.getText();
        this.base64Image = conclusionPage.getBase64Image();
    }

}
