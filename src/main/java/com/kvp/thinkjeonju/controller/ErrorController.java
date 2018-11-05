package com.kvp.thinkjeonju.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/loginError")
    public String getLoginErrorPage() {
        return "loginError";
    }
}
