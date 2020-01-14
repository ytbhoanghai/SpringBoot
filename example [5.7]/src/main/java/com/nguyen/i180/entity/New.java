package com.nguyen.i180.entity;

import lombok.Data;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.persistence.*;

@Data
@Entity
public class New {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_new") private Integer id;

    private String content;
}
