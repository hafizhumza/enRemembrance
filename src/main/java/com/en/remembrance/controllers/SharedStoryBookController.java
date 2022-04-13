package com.en.remembrance.controllers;

import com.en.remembrance.constants.Constant;
import com.en.remembrance.domain.StoryBook;
import com.en.remembrance.dtos.StoryBookModel;
import com.en.remembrance.exceptions.StoryBookExpiredException;
import com.en.remembrance.exceptions.StoryBookNotFoundException;
import com.en.remembrance.services.CategoryService;
import com.en.remembrance.services.FileService;
import com.en.remembrance.services.SharedStoryBookService;
import com.en.remembrance.services.StoryBookService;
import com.en.remembrance.services.UserService;
import com.en.remembrance.utilities.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;


@Slf4j
@Transactional(readOnly = true)
@RequestMapping("/shared/storybooks")
@Controller
public class SharedStoryBookController extends BaseController {

    private final SharedStoryBookService sharedStoryBookService;

    private final StoryBookService storyBookService;

    private final CategoryService categoryService;

    private final UserService userService;

    private final FileService fileService;

    @Autowired
    public SharedStoryBookController(SharedStoryBookService sharedStoryBookService, StoryBookService storyBookService, CategoryService categoryService, UserService userService, FileService fileService) {
        this.sharedStoryBookService = sharedStoryBookService;
        this.storyBookService = storyBookService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping("/{token}")
    public String storyBooks(@PathVariable String token, @RequestParam(required = false) Integer page, HttpSession session, Model model, @ModelAttribute(Constant.KEY_RESULT_MESSAGE) String resultMessage, @ModelAttribute(Constant.KEY_ERROR) String errorMessage) {
        StoryBook storyBook = null;
        boolean result = false;

        if (page == null) {
            page = 1;
        }

        try {
            storyBook = sharedStoryBookService.getSharedStoryBook(token);
            result = true;
        } catch (StoryBookNotFoundException e) {
            model.addAttribute(Constant.KEY_ERROR, "Storybook not found");
        } catch (StoryBookExpiredException e) {
            model.addAttribute(Constant.KEY_ERROR, "Link has been expired");
        } catch (Exception e) {
            model.addAttribute(Constant.KEY_ERROR, "An unknown error occurred");
        }

        if (!result) {
            return "invalid-link";
        }

        StoryBookModel storyBookModel = storyBookService.getStoryBookModel(storyBook);
        model.addAttribute(Constant.KEY_TOKEN, token);

        if (Util.between(page, Constant.TITLE_PAGE_NUM, Constant.CONCLUSION_PAGE_NUM)) {
            model.addAttribute(Constant.KEY_PAGE, page);
            model.addAttribute(Constant.KEY_PAGE_MODEL, storyBookModel.getPages().get(page - 2));
            return "storybook/shared/mid-page";
        } else if (page >= Constant.CONCLUSION_PAGE_NUM) {
            model.addAttribute(Constant.KEY_PAGE, Constant.CONCLUSION_PAGE_NUM);
            model.addAttribute(Constant.KEY_CONCLUSION_PAGE_MODEL, storyBookModel.getConclusionPage());
            return "storybook/shared/conclusion-page";
        }

        model.addAttribute(Constant.KEY_PAGE, Constant.TITLE_PAGE_NUM);
        model.addAttribute(Constant.KEY_TITLE_PAGE_MODEL, storyBookModel.getTitlePage());
        model.addAttribute(Constant.KEY_CATEGORY_NAME, storyBookModel.getCategoryName());
        return "storybook/shared/title-page";
    }

}
