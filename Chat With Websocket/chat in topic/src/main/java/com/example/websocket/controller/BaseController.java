package com.example.websocket.controller;

import com.example.websocket.security.CustomUserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class BaseController {

    @GetMapping
    public String doGetIndex(Authentication authentication, Model model) {
        model.addAttribute("fullName", ((CustomUserDetail) authentication.getPrincipal()).user.getFullName());
        return "index";
    }

}
