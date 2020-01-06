package com.nguyen.springboot;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class HandlerException {

    @ExceptionHandler(AbcException.class)
    public String HandlerAbcException(Model model) {
        model.addAttribute("response", "AbcException: " + new Date());
        return "error";
    }
}
