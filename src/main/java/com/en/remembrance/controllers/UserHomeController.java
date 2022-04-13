package com.en.remembrance.controllers;

import com.en.remembrance.dtos.ChangePasswordRequest;
import com.en.remembrance.dtos.UpdateUserRequest;
import com.en.remembrance.constants.Constant;
import com.en.remembrance.controllers.BaseController;
import com.en.remembrance.dtos.UserDto;
import com.en.remembrance.services.StoryBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;


@RequestMapping("/user")
@Controller
public class UserHomeController extends BaseController {

    private final StoryBookService storyBookService;

    @Autowired
    public UserHomeController(StoryBookService storyBookService) {
        this.storyBookService = storyBookService;
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model, @ModelAttribute(Constant.KEY_RESULT_MESSAGE) String resultMessage, @ModelAttribute(Constant.KEY_ERROR) String errorMessage) {
        UserDto currentUser = getCurrentUserDto();
        model.addAttribute(Constant.KEY_USER, currentUser);
        model.addAttribute(Constant.KEY_STORIES_COUNT, storyBookService.countByUserId(currentUser.getId()));

        model.addAttribute(Constant.KEY_ERROR, errorMessage);
        model.addAttribute(Constant.KEY_RESULT_MESSAGE, resultMessage);
        return "user/home";
    }

    @GetMapping("/profile")
    public String profile(@RequestParam(required = false) String mode, Model model, @ModelAttribute(Constant.KEY_RESULT_MESSAGE) String resultMessage, @ModelAttribute(Constant.KEY_ERROR) String errorMessage) {
        UserDto user = setUserModel(model);
        model.addAttribute(Constant.KEY_MODE, mode);
        model.addAttribute(Constant.KEY_CHANGE_PASSWORD_MODEL, new ChangePasswordRequest());
        model.addAttribute(Constant.KEY_UPDATE_PROFILE_MODEL, new UpdateUserRequest(user.getName(), new Date(user.getDob()), user.getEmail()));
        model.addAttribute(Constant.KEY_ERROR, errorMessage);
        model.addAttribute(Constant.KEY_RESULT_MESSAGE, resultMessage);
        return "user/profile";
    }

 /*   @GetMapping("users")
    public String users(@RequestParam(value = "mode", required = false, defaultValue = "all") String mode,
                        @ModelAttribute(UiConst.KEY_RESULT_MESSAGE) String resultMessage,
                        @ModelAttribute(UiConst.KEY_ERROR) String errorMessage,
                        HttpSession session, Model model) {

        UserSession currentUser = getVerifiedUser(session);
        model.addAttribute(UiConst.KEY_USERS, userService.listActiveUsers(getBearerToken(currentUser)));
        model.addAttribute(UiConst.KEY_USER, currentUser);
        model.addAttribute(UiConst.KEY_ERROR, (errorMessage != null && errorMessage.equals("")) ? null : errorMessage);
        model.addAttribute(UiConst.KEY_RESULT_MESSAGE, (resultMessage != null && resultMessage.equals("")) ? null : resultMessage);
        return "user/users";
    }*/

}
