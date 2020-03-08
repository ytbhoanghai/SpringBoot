package com.nguyen.websocket.controller;

import com.nguyen.websocket.entity.User;
import com.nguyen.websocket.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class IndexController {

    @GetMapping
    public String doGetIndex(Authentication auth, Model model) {
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        model.addAttribute("fullName", user.getFullName());
        return "index";
    }
}
