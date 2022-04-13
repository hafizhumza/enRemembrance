package com.en.remembrance.domain;

import com.en.remembrance.constants.PagePosition;
import com.en.remembrance.dtos.SegmentModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "segment")
public class Segment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column
    private String text;

    @Column
    private Boolean hasText;

    @Lob
    @Column(name = "image_path")
    private String imagePath;

    @Enumerated(EnumType.STRING)
    @Column
    private PagePosition position;

    @ManyToOne
    @JoinColumn(name = "page_id", referencedColumnName = "id")
    private Page page;

    @Transient
    private String base64Image;

    public Segment(SegmentModel segmentModel, PagePosition position) {
        this.id = segmentModel.getId();
        this.text = segmentModel.getText();
        this.hasText = segmentModel.getHasText() == 1;
        this.position = position;
    }

}
