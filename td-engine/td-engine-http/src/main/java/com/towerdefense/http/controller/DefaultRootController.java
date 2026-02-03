package com.towerdefense.http.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultRootController {

    @GetMapping("/")
    public String home() {
        return "Tower Defense Game API is running.";
    }
}
