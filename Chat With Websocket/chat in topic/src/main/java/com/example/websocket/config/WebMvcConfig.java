package com.example.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/bootstrap/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/bootstrap/4.4.1-1/");
        registry.addResourceHandler("/resources/jquery/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/jquery/3.4.0/");
        registry.addResourceHandler("/resources/popper/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/popper.js/1.14.3/");

        registry.addResourceHandler("/resources/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/resources/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/resources/img/**").addResourceLocations("classpath:/static/img/");
    }
}
