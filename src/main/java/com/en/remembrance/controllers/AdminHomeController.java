package com.en.remembrance.controllers;

import com.en.remembrance.constants.Constant;
import com.en.remembrance.dtos.ChangePasswordRequest;
import com.en.remembrance.dtos.StoryBookModel;
import com.en.remembrance.dtos.UpdateUserRequest;
import com.en.remembrance.dtos.UserDto;
import com.en.remembrance.exceptions.InvalidAccessException;
import com.en.remembrance.exceptions.StoryBookNotFoundException;
import com.en.remembrance.services.StoryBookService;
import com.en.remembrance.utilities.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;


@Slf4j
@Transactional(readOnly = true)
@RequestMapping("/admin")
@Controller
public class AdminHomeController extends BaseController {

    private final StoryBookService storyBookService;

    @Autowired
    public AdminHomeController(StoryBookService storyBookService) {
        this.storyBookService = storyBookService;
    }

    @GetMapping("/home")
    public String home(Model model, @ModelAttribute(Constant.KEY_RESULT_MESSAGE) String resultMessage, @ModelAttribute(Constant.KEY_ERROR) String errorMessage) {
        setUserModel(model);
        setMessages(model, resultMessage, errorMessage);
        model.addAttribute(Constant.KEY_ALL_STORIES_COUNT, storyBookService.countAllStories());
        model.addAttribute(Constant.KEY_ACTIVE_STORIES_COUNT, storyBookService.countActiveStories());
        model.addAttribute(Constant.KEY_TRASHED_STORIES_COUNT, storyBookService.countTrashedStories());
        return "admin/home";
    }

    @GetMapping("/storybooks/home")
    public String storyBooks(@RequestParam(required = false) String mode, Model model, @ModelAttribute(Constant.KEY_RESULT_MESSAGE) String resultMessage, @ModelAttribute(Constant.KEY_ERROR) String errorMessage) {
        setUserModel(model);
        setMessages(model, resultMessage, errorMessage);

        if (StringUtils.hasText(mode) && mode.equals("active")) {
            model.addAttribute(Constant.KEY_STORY_BOOKS, storyBookService.findActiveStoryBookListModel());
        } else {
            model.addAttribute(Constant.KEY_STORY_BOOKS, storyBookService.findAllStoryBookListModel());
        }

        return "admin/storybook-home";
    }

    @GetMapping("/storybooks/trash")
    public String trash(Model model, @ModelAttribute(Constant.KEY_RESULT_MESSAGE) String resultMessage, @ModelAttribute(Constant.KEY_ERROR) String errorMessage) {
        setUserModel(model);
        setMessages(model, resultMessage, errorMessage);
        model.addAttribute(Constant.KEY_STORY_BOOKS, storyBookService.findInActiveStoryBookListModel());
        return "admin/storybook-trash";
    }

    @Transactional
    @GetMapping("/storybooks/recycle/{id}")
    public String recycle(@PathVariable long id, RedirectAttributes redirectAttributes) {
        boolean result = false;
        String message = "Storybook has been sent to trash";

        try {
            result = storyBookService.recycle(id);
        } catch (StoryBookNotFoundException e) {
            message = "Storybook not found";
        } catch (Exception e) {
            message = "Unknown error";
        }

        if (result) {
            redirectAttributes.addFlashAttribute(Constant.KEY_RESULT_MESSAGE, message);
        } else {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, message);
        }

        return "redirect:/admin/storybooks/home";
    }

    @Transactional
    @GetMapping("/storybooks/restore/{id}")
    public String restore(@PathVariable long id, RedirectAttributes redirectAttributes) {
        boolean result = false;
        String message = "Storybook has been restored successfully!";

        try {
            result = storyBookService.restore(id);
        } catch (StoryBookNotFoundException e) {
            message = "Storybook not found";
        } catch (Exception e) {
            message = "Unknown error";
        }

        if (result) {
            redirectAttributes.addFlashAttribute(Constant.KEY_RESULT_MESSAGE, message);
        } else {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, message);
        }

        return "redirect:/admin/storybooks/home";
    }

    @Transactional
    @GetMapping("/storybooks/delete/{id}")
    public String delete(@PathVariable long id, RedirectAttributes redirectAttributes) {
        boolean result = false;
        String message = "Storybook has been deleted";

        try {
            result = storyBookService.delete(id);
        } catch (StoryBookNotFoundException e) {
            message = "Storybook not found";
        } catch (InvalidAccessException e) {
            message = "Send storybook in trash first";
        } catch (Exception e) {
            message = "Unknown error";
        }

        if (result) {
            redirectAttributes.addFlashAttribute(Constant.KEY_RESULT_MESSAGE, message);
        } else {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, message);
        }

        return "redirect:/admin/storybooks/home";
    }

    @GetMapping("/storybooks/{id}")
    public String storyBook(@PathVariable long id, @RequestParam(required = false) String page, Model model) {
        setUserModel(model);
        StoryBookModel storyBookModel = storyBookService.findById(id);

        model.addAttribute(Constant.KEY_STORY_BOOK_ID, id);

        int pageNumber;

        try {
            pageNumber = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            log.info("[ViewStory] Exception while parsing page: {}", e.getMessage());
            pageNumber = Constant.TITLE_PAGE_NUM;

            if (StringUtils.hasText(page)) {
                model.addAttribute(Constant.KEY_ERROR, "Invalid Page");
            }
        }

        if (Util.between(pageNumber, Constant.TITLE_PAGE_NUM, Constant.CONCLUSION_PAGE_NUM)) {
            model.addAttribute(Constant.KEY_PAGE, pageNumber);
            model.addAttribute(Constant.KEY_PAGE_MODEL, storyBookModel.getPages().get(pageNumber - 2));
            return "storybook/admin/mid-page";
        } else if (pageNumber >= Constant.CONCLUSION_PAGE_NUM) {
            model.addAttribute(Constant.KEY_PAGE, Constant.CONCLUSION_PAGE_NUM);
            model.addAttribute(Constant.KEY_CONCLUSION_PAGE_MODEL, storyBookModel.getConclusionPage());
            return "storybook/admin/conclusion-page";
        }

        model.addAttribute(Constant.KEY_PAGE, Constant.TITLE_PAGE_NUM);
        model.addAttribute(Constant.KEY_TITLE_PAGE_MODEL, storyBookModel.getTitlePage());
        model.addAttribute(Constant.KEY_CATEGORY_NAME, storyBookModel.getCategoryName());
        return "storybook/admin/title-page";
    }

    @GetMapping("/profile")
    public String profile(@RequestParam(required = false) String mode, HttpSession session, Model model, @ModelAttribute(Constant.KEY_RESULT_MESSAGE) String resultMessage, @ModelAttribute(Constant.KEY_ERROR) String errorMessage) {
        UserDto user = setUserModel(model);
        model.addAttribute(Constant.KEY_MODE, mode);
        model.addAttribute(Constant.KEY_CHANGE_PASSWORD_MODEL, new ChangePasswordRequest());
        model.addAttribute(Constant.KEY_UPDATE_PROFILE_MODEL, new UpdateUserRequest(user.getName(), new Date(user.getDob()), user.getEmail()));
        model.addAttribute(Constant.KEY_ERROR, errorMessage);
        model.addAttribute(Constant.KEY_RESULT_MESSAGE, resultMessage);
        return "admin/profile";
    }

}
