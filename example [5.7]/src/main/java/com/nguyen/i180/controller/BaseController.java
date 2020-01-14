package com.nguyen.i180.controller;

import com.nguyen.i180.entity.New;
import com.nguyen.i180.repository.NewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/news")
public class BaseController {

    private NewRepository newRepository;

    public BaseController(@Autowired NewRepository newRepository) {
        this.newRepository = newRepository;
    }

    @GetMapping
    public ResponseEntity<List<New>> doGetNews() {
        List<New> news = newRepository.findAll();
        if (news.size() == 0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(news);
    }
}
