package com.en.remembrance.dtos;

import com.en.remembrance.domain.Segment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SegmentModel {

    private Long id;

    private int hasText;

    private String text;

    private MultipartFile image;

    private String imageType;

    private String base64Image;

    public SegmentModel(Segment segment) {
        this.id = segment.getId();
        this.hasText = segment.getHasText() ? 1 : 0;
        this.text = segment.getText();
        this.base64Image = segment.getBase64Image();
    }

}
