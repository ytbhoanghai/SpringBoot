package com.nguyen.i18n.controller;

import com.nguyen.i18n.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    private static final String USER_NAME;
    private static final String PASSWORD;

    static {
        USER_NAME = "root";
        PASSWORD = "123";
    }

    @GetMapping
    public String doGetLogin(User user) {
        return "login";
    }

    @PostMapping
    public String doPostLogin(User user, Model model) {
        if (user.getUserName().equals(USER_NAME) && user.getPassword().equals(PASSWORD)) {
            model.addAttribute("hello_userName", user.getUserName());
        }
        else {
            model.addAttribute("loginFailed", true);
        }
        return "login";
    }

}
