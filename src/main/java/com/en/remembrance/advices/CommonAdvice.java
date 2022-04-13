package com.en.remembrance.advices;

import com.en.remembrance.exceptions.CategoryNotFoundException;
import com.en.remembrance.exceptions.InvalidAccessException;
import com.en.remembrance.exceptions.RecordNotFoundException;
import com.en.remembrance.exceptions.ResponseException;
import com.en.remembrance.exceptions.RoleNotFoundException;
import com.en.remembrance.exceptions.StoryBookNotFoundException;
import com.en.remembrance.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.client.HttpClientErrorException.Forbidden;
import static org.springframework.web.client.HttpClientErrorException.Unauthorized;


@Slf4j
@ControllerAdvice
public class CommonAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({InvalidAccessException.class, Unauthorized.class, Forbidden.class})
    ModelAndView handleUnAuthorizeException(HttpServletRequest req, Exception ex) {
        log.error(ex.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("403");
        return modelAndView;
    }

    @ExceptionHandler({RecordNotFoundException.class, HttpClientErrorException.NotFound.class, CategoryNotFoundException.class, StoryBookNotFoundException.class, UserNotFoundException.class, RoleNotFoundException.class})
    ModelAndView handleNotFoundException(HttpServletRequest req, Exception ex) {
        log.error(ex.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404");
        return modelAndView;
    }

    @ExceptionHandler({ResponseException.class, Exception.class})
    ModelAndView handleInternalServerException(HttpServletRequest req, Exception ex) {
        log.error(ex.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        return modelAndView;
    }

}
