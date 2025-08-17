package com.saasplatform.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@CrossOrigin
public class PublicController {

    @GetMapping("/health")
    public String health() {
        return "Public API is accessible";
    }

    @GetMapping("/info")
    public String info() {
        return "SaaS Platform API v1.0";
    }
}

