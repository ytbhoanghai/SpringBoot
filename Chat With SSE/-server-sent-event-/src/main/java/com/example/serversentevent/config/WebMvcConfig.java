package com.example.serversentevent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String PATH_PATTERN = "/**";
    private static final String[] METHODS = {"GET", "POST", "PUT", "DELETE"};
    private static final String[] ORIGINS = {"http://localhost:63343"};
    private static final String[] HEADERS = {"*"};
    private static final Boolean CREDENTIAL = true;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping(PATH_PATTERN)
                .allowedMethods(METHODS)
                .allowedOrigins(ORIGINS)
                .allowedHeaders(HEADERS)
                .allowCredentials(CREDENTIAL);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
    }
}
