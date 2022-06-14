package com.flow.eda.oauth2.handler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestOauth2Controller {

    @GetMapping("/oauth2/hello")
    public String admin() {
        return "hello oauth2!";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello!";
    }
}
