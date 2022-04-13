package com.en.remembrance.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "story_book")
public class StoryBook implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date")
    protected Long createdDate;

    @Column(name = "modified_date")
    protected Long modifiedDate;

    @Column
    private Boolean recycled;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AuthUser user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "title_page_id", referencedColumnName = "id")
    private TitlePage titlePage;

    @OneToMany(mappedBy = "storyBook", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Page> pages;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "conclusion_page_id", referencedColumnName = "id")
    private ConclusionPage conclusionPage;

    @OneToMany(mappedBy = "storyBook", cascade = CascadeType.REMOVE)
    private List<SharedStoryBook> sharedStoryBooks;

    @PrePersist
    public void onCreate() {
        createdDate = System.currentTimeMillis();
    }

    @PreUpdate
    public void onUpdate() {
        modifiedDate = System.currentTimeMillis();
    }

}
