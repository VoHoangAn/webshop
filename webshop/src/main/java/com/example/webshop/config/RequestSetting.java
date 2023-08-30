package com.example.webshop.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.Principal;

@RestControllerAdvice
@RequiredArgsConstructor
public class RequestSetting {
    private final static Logger LOGGER = LoggerFactory.getLogger(RequestSetting.class);
    @ModelAttribute("currentUsername")
    String currentUser(Principal principal) {
        return (principal!=null)? principal.getName() : null;
    }
}
