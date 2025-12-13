package com.example.cosmocatsmarket.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestSecuredController {
    @GetMapping("/test/secure")
    public String secure() {
        return "ok";
    }
}
