package com.aps.tiktube.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
    @GetMapping("/getuser")
    public String getUserString() {
        return "test";
    }
    
}
