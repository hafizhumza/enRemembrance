package com.en.remembrance.domain;

import com.en.remembrance.dtos.TitlePageModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "title_page")
public class TitlePage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String author;

    @Lob
    @Column(name = "image_path")
    private String imagePath;

    @OneToOne
    @JoinColumn(name = "story_book_id", referencedColumnName = "id")
    private StoryBook storyBook;

    @Transient
    private String base64Image;

    public TitlePage(TitlePageModel titlePageModel) {
        this.id = titlePageModel.getId();
        this.title = titlePageModel.getTitle();
        this.author = titlePageModel.getAuthor();
    }

}
