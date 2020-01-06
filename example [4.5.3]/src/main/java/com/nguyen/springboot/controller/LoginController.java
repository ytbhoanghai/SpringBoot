package com.nguyen.springboot.controller;

import com.nguyen.springboot.AbcException;
import com.nguyen.springboot.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/")
public class LoginController {

    @GetMapping("login")
    public String doGetLogin(User user) {
        return "login";
    }

    @PostMapping("login")
    public String doPostLogin(@Validated User user, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            String response = "user not found";
            if (LoginController.serviceLogin(user) != null) {
                response = "Hello " + user.getUsername();
            }
            model.addAttribute("response", response);
        }
        return "login";
    }

    @GetMapping(value = "exception")
    public String createException() {
        throw new AbcException();
    }

    private static User serviceLogin(User user) {
        if (user.getUsername().equalsIgnoreCase("root")
                && user.getPassword().equalsIgnoreCase("123")) {
            return user;
        }
        return null;
    }
}
