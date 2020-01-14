package com.nguyen.i18n.controller;

import com.nguyen.i18n.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/login", "/en/login", "/vi/login"})
public class LoginController {

    private static final String USER_NAME = "root";
    private static final String PASSWORD = "123";

    @GetMapping
    public String doGetLogin(User user) {
        return "login";
    }

    @PostMapping
    public String doPostLogin(@Validated User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }
        if (!LoginController.checkInfo(user)) {
            model.addAttribute("loginFailed", true);
        }
        else {
            model.addAttribute("hello_userName", user.getUserName());
        }
        return "login";
    }

    private static boolean checkInfo(User user) {
        if (user.getUserName().equals(USER_NAME) && user.getPassword().equals(PASSWORD)) {
            return true;
        }
        return false;
    }
}
