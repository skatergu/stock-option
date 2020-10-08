package com.aj.options.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {
    @GetMapping("/helloWorld")
    public String helloWorld() {
        return "Hello World";
    }
}
