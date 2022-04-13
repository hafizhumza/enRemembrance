package com.en.remembrance.dtos;

import com.en.remembrance.constants.Constant;
import com.en.remembrance.domain.StoryBook;
import com.en.remembrance.utilities.Util;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
public class StoryBookModel {

    private Long id;

    private Long userId;

    private String categoryName;

    private String createDate;

    private Boolean recycled;

    private String modifyDate;

    private TitlePageModel titlePage;

    private List<PageModel> pages;

    private ConclusionPageModel conclusionPage;

    public StoryBookModel() {
        pages = new ArrayList<>(Constant.PAGES_ALLOWED);

        for (int i = 0; i < Constant.PAGES_ALLOWED; i++) {
            pages.add(new PageModel());
        }

        titlePage = null;
        conclusionPage = new ConclusionPageModel();
    }

    public StoryBookModel(StoryBook storyBook) {
        this.id = storyBook.getId();
        this.createDate = Util.formatDate(storyBook.getCreatedDate());
        this.modifyDate = Util.formatDate(storyBook.getModifiedDate());
        this.recycled = storyBook.getRecycled();
        this.userId = storyBook.getUser().getId();
        this.categoryName = storyBook.getCategory().getName();
        this.titlePage = new TitlePageModel(storyBook.getTitlePage());
        this.conclusionPage = new ConclusionPageModel(storyBook.getConclusionPage());

        if (!CollectionUtils.isEmpty(storyBook.getPages())) {
            pages = storyBook.getPages().stream().map(PageModel::new).collect(Collectors.toList());
        }
    }

}
