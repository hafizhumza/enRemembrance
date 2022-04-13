package com.en.remembrance.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;


@Setter
@Getter
@Entity
@Table(name = "shared_story_book")
public class SharedStoryBook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Lob
    @Column
    private String token;

    @Column(name = "create_date")
    private Long createDate;

    @ManyToOne
    @JoinColumn(name = "story_book_id")
    private StoryBook storyBook;

    @PrePersist
    protected void onCreate() {
        createDate = System.currentTimeMillis();
    }

}
