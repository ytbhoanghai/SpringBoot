package com.nguyen.demo.controller;

import com.nguyen.demo.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class BaseController {

    @GetMapping
    public String getIndex(Authentication auth, Model model) {
        model.addAttribute("fullName", ((CustomUserDetails) auth.getPrincipal()).getFullName());
        return "index";
    }
}
