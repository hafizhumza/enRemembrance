package com.en.remembrance.controllers;

import com.en.remembrance.constants.Constant;
import com.en.remembrance.dtos.ConclusionPageModel;
import com.en.remembrance.dtos.PageModel;
import com.en.remembrance.dtos.ShareStoryBookRequest;
import com.en.remembrance.dtos.StoryBookListModel;
import com.en.remembrance.dtos.StoryBookModel;
import com.en.remembrance.dtos.TitlePageModel;
import com.en.remembrance.dtos.UserDto;
import com.en.remembrance.exceptions.CategoryNotFoundException;
import com.en.remembrance.exceptions.StoryBookNotFoundException;
import com.en.remembrance.exceptions.UserNotFoundException;
import com.en.remembrance.services.CategoryService;
import com.en.remembrance.services.FileService;
import com.en.remembrance.services.StoryBookService;
import com.en.remembrance.services.UserService;
import com.en.remembrance.utilities.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@Slf4j
@Transactional(readOnly = true)
@RequestMapping("/storybooks")
@Controller
public class StoryBookController extends BaseController {

    private final StoryBookService storyBookService;

    private final CategoryService categoryService;

    private final UserService userService;

    private final FileService fileService;

    @Autowired
    public StoryBookController(StoryBookService storyBookService, CategoryService categoryService, UserService userService, FileService fileService) {
        this.storyBookService = storyBookService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping("/home")
    public String storyBooks(Model model, @ModelAttribute(Constant.KEY_RESULT_MESSAGE) String resultMessage, @ModelAttribute(Constant.KEY_ERROR) String errorMessage) {
        UserDto currentUser = setUserModel(model);
        List<StoryBookListModel> stories = storyBookService.findStoryBookListModelByUserId(currentUser.getId());
        model.addAttribute(Constant.KEY_STORY_BOOKS, stories);

        setMessages(model, resultMessage, errorMessage);
        return "storybook/home";
    }

    @GetMapping("/{id}")
    public String storyBook(@PathVariable long id, @RequestParam(required = false) String page, Model model) {
        UserDto currentUser = setUserModel(model);
        StoryBookModel storyBookModel = storyBookService.findByIdAndUserId(id, currentUser.getId());

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
            return "storybook/mid-page";
        } else if (pageNumber >= Constant.CONCLUSION_PAGE_NUM) {
            model.addAttribute(Constant.KEY_PAGE, Constant.CONCLUSION_PAGE_NUM);
            model.addAttribute(Constant.KEY_CONCLUSION_PAGE_MODEL, storyBookModel.getConclusionPage());
            return "storybook/conclusion-page";
        }

        model.addAttribute(Constant.KEY_PAGE, Constant.TITLE_PAGE_NUM);
        model.addAttribute(Constant.KEY_TITLE_PAGE_MODEL, storyBookModel.getTitlePage());
        model.addAttribute(Constant.KEY_CATEGORY_NAME, storyBookModel.getCategoryName());
        return "storybook/title-page";
    }

    @GetMapping("/create")
    public String createGet(@RequestParam(required = false) String page, HttpSession session, Model model) {
        setUserModel(model);
        setTitleAndConclusionPagesKey(model);

        int pageNumber;

        try {
            pageNumber = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            log.info("[CreateStory] Exception while parsing page: {}", e.getMessage());
            pageNumber = Constant.TITLE_PAGE_NUM;

            if (StringUtils.hasText(page)) {
                model.addAttribute(Constant.KEY_ERROR, "Invalid Page");
            }
        }

        StoryBookModel storyBookModel = (StoryBookModel) session.getAttribute(Constant.SESSION_CREATE_STORY_BOOK);

        if (pageNumber > Constant.CONCLUSION_PAGE_NUM) {
            if (storyBookModel != null)
                pageNumber = Constant.CONCLUSION_PAGE_NUM;
            else
                pageNumber = Constant.TITLE_PAGE_NUM;

            model.addAttribute(Constant.KEY_ERROR, "Invalid Page");
        }

        if (storyBookModel == null) {
            if (pageNumber != Constant.TITLE_PAGE_NUM) {
                pageNumber = Constant.TITLE_PAGE_NUM;
                model.addAttribute(Constant.KEY_ERROR, "Please add Title Page information first");
            }

            model.addAttribute(Constant.KEY_TITLE_PAGE_MODEL, new TitlePageModel());
        } else {
            if (pageNumber == Constant.TITLE_PAGE_NUM) {
                model.addAttribute(Constant.KEY_TITLE_PAGE_MODEL, storyBookModel.getTitlePage());

                /*TitlePageModel titlePage = storyBookModel.getTitlePage();

                if (titlePage != null) {
                    model.addAttribute(Constant.KEY_TITLE_PAGE_MODEL, titlePage);
                } else {
                    model.addAttribute(Constant.KEY_TITLE_PAGE_MODEL, new TitlePageModel());
                    model.addAttribute(Constant.KEY_ERROR, "Please add Title Page information first");
                }*/
            } else if (Util.between(pageNumber, Constant.TITLE_PAGE_NUM, Constant.CONCLUSION_PAGE_NUM)) {
                model.addAttribute(Constant.KEY_PAGE_MODEL, storyBookModel.getPages().get(pageNumber - 2));
            } else if (pageNumber == Constant.CONCLUSION_PAGE_NUM) {
                model.addAttribute(Constant.KEY_CONCLUSION_PAGE_MODEL, storyBookModel.getConclusionPage());
            } else {
                pageNumber = Constant.TITLE_PAGE_NUM;
                model.addAttribute(Constant.KEY_ERROR, "Invalid Page");
            }
        }

        switch (pageNumber) {
            case Constant.TITLE_PAGE_NUM:
                model.addAttribute(Constant.KEY_CATEGORIES, categoryService.getAllCategoryModels());
                break;
            case Constant.CONCLUSION_PAGE_NUM:
                break;
            default: // Middle pages
                break;
        }

        model.addAttribute(Constant.KEY_PAGE, pageNumber);
        return "storybook/create";
    }

    @PostMapping("/create/titlePage")
    public String createTitlePage(@Valid TitlePageModel titlePageModel, BindingResult bindingResult, HttpSession session, Model model) {
        UserDto currentUser = setUserModel(model);
        setTitleAndConclusionPagesKey(model);

        StoryBookModel sessionModel = (StoryBookModel) session.getAttribute(Constant.SESSION_CREATE_STORY_BOOK);

        if (bindingResult.hasErrors() || (titlePageModel.getImage() == null || titlePageModel.getImage().isEmpty())) {
            model.addAttribute(Constant.KEY_CATEGORIES, categoryService.getAllCategoryModels());
            model.addAttribute(Constant.KEY_TITLE_PAGE_MODEL, titlePageModel);
            model.addAttribute(Constant.KEY_PAGE, Constant.TITLE_PAGE_NUM);

            if (titlePageModel.getImage() == null || titlePageModel.getImage().isEmpty())
                model.addAttribute(Constant.KEY_IMAGE_ERROR, "Please select an image");

            return "storybook/create";
        } else {
            try {
                titlePageModel.setImageType(fileService.getFileType(titlePageModel.getImage()));
                titlePageModel.setBase64Image(fileService.convertToBase64(titlePageModel.getImage()));
            } catch (IOException e) {
                log.error("[TitlePage] Exception while converting image to base64: ", e);
            }

            if (sessionModel == null) {
                StoryBookModel storyBookModel = new StoryBookModel();
                storyBookModel.setUserId(currentUser.getId());
                storyBookModel.setTitlePage(titlePageModel);
                session.setAttribute(Constant.SESSION_CREATE_STORY_BOOK, storyBookModel);
            } else {
                sessionModel.setTitlePage(titlePageModel);
            }

            return "redirect:/storybooks/create?page=2";
        }
    }

    @PostMapping("/create")
    public String createPage(@RequestParam int page, @RequestParam(required = false) boolean routeToLast, PageModel pageModel, HttpSession session, Model model) {
        setUserModel(model);
        setTitleAndConclusionPagesKey(model);

        StoryBookModel storyBookModel = (StoryBookModel) session.getAttribute(Constant.SESSION_CREATE_STORY_BOOK);

        if (storyBookModel == null) {
            model.addAttribute(Constant.KEY_ERROR, "Please add Title Page information first");
            return prepareTitlePageModel(model);
        } else {
            if (Util.between(page, Constant.TITLE_PAGE_NUM, Constant.CONCLUSION_PAGE_NUM)) {

                try {
                    pageModel.getTopLeftSegment().setImageType(fileService.getFileType(pageModel.getTopLeftSegment().getImage()));
                    pageModel.getTopRightSegment().setImageType(fileService.getFileType(pageModel.getTopRightSegment().getImage()));
                    pageModel.getBottomLeftSegment().setImageType(fileService.getFileType(pageModel.getBottomLeftSegment().getImage()));
                    pageModel.getBottomRightSegment().setImageType(fileService.getFileType(pageModel.getBottomRightSegment().getImage()));

                    pageModel.getTopLeftSegment().setBase64Image(fileService.convertToBase64(pageModel.getTopLeftSegment().getImage()));
                    pageModel.getTopRightSegment().setBase64Image(fileService.convertToBase64(pageModel.getTopRightSegment().getImage()));
                    pageModel.getBottomLeftSegment().setBase64Image(fileService.convertToBase64(pageModel.getBottomLeftSegment().getImage()));
                    pageModel.getBottomRightSegment().setBase64Image(fileService.convertToBase64(pageModel.getBottomRightSegment().getImage()));
                } catch (IOException e) {
                    log.error("[MiddlePages] Exception while converting image to base64: ", e);
                }

                storyBookModel.getPages().set(page - 2, pageModel);
                page++;
            } else {
                model.addAttribute(Constant.KEY_ERROR, "Invalid Page");
                return prepareTitlePageModel(model);
            }
        }

        if (routeToLast) {
            page = Constant.CONCLUSION_PAGE_NUM;
        }

        return "redirect:/storybooks/create?page=" + (page);
    }

    @Transactional
    @PostMapping("/create/story")
    public String createStory(@Valid ConclusionPageModel conclusionPageModel, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        StoryBookModel sessionModel = (StoryBookModel) session.getAttribute(Constant.SESSION_CREATE_STORY_BOOK);

        if (sessionModel == null) {
            model.addAttribute(Constant.KEY_ERROR, "Please add Title Page information first");
            return prepareTitlePageModel(model);
        }

        int hasText = conclusionPageModel.getHasText();

        boolean isNotValidText = hasText == 1 && !StringUtils.hasText(conclusionPageModel.getText());
        boolean isNotValidImage = hasText == 0 && (conclusionPageModel.getImage() == null || conclusionPageModel.getImage().isEmpty());

        if (bindingResult.hasErrors() || isNotValidText || isNotValidImage) {
            setTitleAndConclusionPagesKey(model);

            if (isNotValidText) {
                model.addAttribute(Constant.KEY_IMAGE_ERROR, "Text field cannot be empty");
            } else if (isNotValidImage) {
                model.addAttribute(Constant.KEY_IMAGE_ERROR, "Please select an image");
            }

            model.addAttribute(Constant.KEY_CONCLUSION_PAGE_MODEL, conclusionPageModel);
            return "storybook/create";
        }

        try {
            conclusionPageModel.setImageType(fileService.getFileType(conclusionPageModel.getImage()));
            conclusionPageModel.setBase64Image(fileService.convertToBase64(conclusionPageModel.getImage()));
        } catch (IOException e) {
            log.error("[ConclusionPage] Exception while converting image to base64: ", e);
        }

        sessionModel.setConclusionPage(conclusionPageModel);

        try {
            storyBookService.save(sessionModel);
        } catch (CategoryNotFoundException e) {
            log.error("Category not found while saving StoryBook: ", e);
            return storyNotCreatedError(conclusionPageModel, model, "Category / Genre not found while creating Storybook");
        } catch (UserNotFoundException e) {
            log.error("User not found while saving StoryBook: ", e);
            return storyNotCreatedError(conclusionPageModel, model, "User not found while creating Storybook");
        } catch (IOException e) {
            log.error("Exception while saving Image: ", e);
            return storyNotCreatedError(conclusionPageModel, model, "An error occurred while saving image");
        } catch (Exception e) {
            log.error("Exception while saving StoryBook: ", e);
            return storyNotCreatedError(conclusionPageModel, model, "An error occurred while creating Storybook");
        }

        session.removeAttribute(Constant.SESSION_CREATE_STORY_BOOK);
        redirectAttributes.addFlashAttribute(Constant.KEY_RESULT_MESSAGE, "Story create successfully");
        return "redirect:/storybooks/home";
    }

    @GetMapping("/share")
    public String share(@RequestParam(required = false) String mode, HttpSession session, Model model, @ModelAttribute(Constant.KEY_RESULT_MESSAGE) String resultMessage, @ModelAttribute(Constant.KEY_ERROR) String errorMessage) {
        UserDto currentUser = setUserModel(model);

        model.addAttribute(Constant.KEY_MODE, mode);

        if (mode == null || !mode.equals("outside")) {
            model.addAttribute(Constant.KEY_USERS, userService.findAllActiveUsersExcludeSelfAndAdmin());
        }

        List<StoryBookListModel> stories = storyBookService.findStoryBookListModelByUserId(currentUser.getId());

        if (CollectionUtils.isEmpty(stories)) {
            model.addAttribute(Constant.KEY_STORY_BOOK_ERROR, "Please create a storybook first");
        }

        model.addAttribute(Constant.KEY_STORY_BOOKS, stories);
        model.addAttribute(Constant.KEY_SHARE_STORY_BOOK, new ShareStoryBookRequest());
        model.addAttribute(Constant.KEY_ERROR, errorMessage);
        model.addAttribute(Constant.KEY_RESULT_MESSAGE, resultMessage);
        return "storybook/share";
    }

    @Transactional
    @PostMapping("/share")
    public String share(@Valid @ModelAttribute ShareStoryBookRequest shareStoryBookRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        UserDto currentUser = setUserModel(model);
        model.addAttribute(Constant.KEY_USERS, userService.findAllActiveUsersExcludeSelfAndAdmin());
        model.addAttribute(Constant.KEY_SHARE_STORY_BOOK, shareStoryBookRequest);

        List<StoryBookListModel> stories = storyBookService.findStoryBookListModelByUserId(currentUser.getId());
        model.addAttribute(Constant.KEY_STORY_BOOKS, stories);

        if (CollectionUtils.isEmpty(stories)) {
            model.addAttribute(Constant.KEY_STORY_BOOK_ERROR, "Please create a storybook first");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute(Constant.KEY_ERROR, bindingResult.getFieldErrors().get(0).getDefaultMessage());
            return "storybook/share";
        }

        boolean result = false;

        try {
            result = storyBookService.share(shareStoryBookRequest, currentUser);
        } catch (MessagingException e) {
            log.error("MessagingException while sharing Storybook: ", e);
            model.addAttribute(Constant.KEY_ERROR, "Error while sending email");
        } catch (ExecutionException e) {
            log.error("ExecutionException while sharing Storybook: ", e);
            model.addAttribute(Constant.KEY_ERROR, "Unknown error while sending email");
        } catch (InterruptedException e) {
            log.error("InterruptedException while sharing Storybook: ", e);
            model.addAttribute(Constant.KEY_ERROR, "Unknown error while sending email");
        }

        if (!result) {
            model.addAttribute(Constant.KEY_ERROR, "Error while sharing Storybook");
            return "storybook/share";
        }

        redirectAttributes.addFlashAttribute(Constant.KEY_RESULT_MESSAGE, "Storybook shared successfully!");
        return "redirect:/storybooks/share";
    }

    @Transactional
    @PostMapping("/share-all")
    public String shareAllWithin(@ModelAttribute ShareStoryBookRequest shareStoryBookRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        UserDto currentUser = setUserModel(model);
        List<UserDto> users = userService.findAllActiveUsersExcludeSelfAndAdmin();

        model.addAttribute(Constant.KEY_USERS, users);
        model.addAttribute(Constant.KEY_SHARE_STORY_BOOK, new ShareStoryBookRequest());

        List<StoryBookListModel> stories = storyBookService.findStoryBookListModelByUserId(currentUser.getId());
        model.addAttribute(Constant.KEY_STORY_BOOKS, stories);

        if (CollectionUtils.isEmpty(stories)) {
            model.addAttribute(Constant.KEY_STORY_BOOK_ERROR, "Please create a storybook first");
        }

        if (CollectionUtils.isEmpty(users)) {
            model.addAttribute(Constant.KEY_ERROR, "Users not found");
            return "storybook/share";
        }

        if (shareStoryBookRequest.getId() == null || shareStoryBookRequest.getId() <= 0) {
            model.addAttribute(Constant.KEY_ERROR, "Invalid Storybook");
            return "storybook/share";
        }

        try {
            List<ShareStoryBookRequest> requests = users.stream()
                    .map(u -> new ShareStoryBookRequest(shareStoryBookRequest.getId(), u.getEmail()))
                    .collect(Collectors.toList());

            storyBookService.share(requests, currentUser);
        } catch (StoryBookNotFoundException e) {
            log.error("StoryBookNotFoundException while sharing Storybook: ", e);
            model.addAttribute(Constant.KEY_ERROR, "Storybook not found");
        } catch (MessagingException e) {
            log.error("MessagingException while sharing Storybook: ", e);
            model.addAttribute(Constant.KEY_ERROR, "Error while sending email");
        } catch (ExecutionException e) {
            log.error("ExecutionException while sharing Storybook: ", e);
            model.addAttribute(Constant.KEY_ERROR, "Unknown error while sending email");
        } catch (InterruptedException e) {
            log.error("InterruptedException while sharing Storybook: ", e);
            model.addAttribute(Constant.KEY_ERROR, "Unknown error while sending email");
        }

        redirectAttributes.addFlashAttribute(Constant.KEY_RESULT_MESSAGE, "Storybook will be shared with all users");
        return "redirect:/storybooks/share";
    }

    private String storyNotCreatedError(ConclusionPageModel conclusionPageModel, Model model, String errorMessage) {
        setUserModel(model);
        setTitleAndConclusionPagesKey(model);
        model.addAttribute(Constant.KEY_ERROR, errorMessage);
        model.addAttribute(Constant.KEY_CONCLUSION_PAGE_MODEL, conclusionPageModel);
        return "storybook/create";
    }

    private void setTitleAndConclusionPagesKey(Model model) {
        model.addAttribute(Constant.KEY_TITLE_PAGE_NUM, Constant.TITLE_PAGE_NUM);
        model.addAttribute(Constant.KEY_CONCLUSION_PAGE_NUM, Constant.CONCLUSION_PAGE_NUM);
    }

    private String prepareTitlePageModel(Model model) {
        model.addAttribute(Constant.KEY_CATEGORIES, categoryService.getAllCategoryModels());
        model.addAttribute(Constant.KEY_PAGE, Constant.TITLE_PAGE_NUM);
        model.addAttribute(Constant.KEY_TITLE_PAGE_MODEL, new TitlePageModel());
        return "storybook/create";
    }

}
