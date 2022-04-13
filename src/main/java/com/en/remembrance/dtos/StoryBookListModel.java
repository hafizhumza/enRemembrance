package com.en.remembrance.dtos;

import com.en.remembrance.domain.StoryBook;
import com.en.remembrance.utilities.Util;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoryBookListModel implements Serializable {

    private Long id;

    private String storyBookName;

    private String userFullName;

    private Boolean recycled;

    private String categoryName;

    private String createDate;

    private String modifyDate;

    public StoryBookListModel(StoryBook storyBook) {
        this.id = storyBook.getId();
        this.userFullName = storyBook.getUser().getFullName();
        this.storyBookName = storyBook.getTitlePage().getTitle();
        this.recycled = storyBook.getRecycled();
        this.categoryName = storyBook.getCategory().getName();
        createDate = Util.formatDate(storyBook.getCreatedDate());
        modifyDate = Util.formatDate(storyBook.getModifiedDate());
    }

}
