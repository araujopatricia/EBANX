package com.ebanx.techtest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class DocController {
    public class HomeController {
        @GetMapping("/")
        public ResponseEntity<Void> home() {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/swagger-ui/index.html"))
                    .build();
        }
    }
}
