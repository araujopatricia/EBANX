package com.ebanx.techtest.controller;

import com.ebanx.techtest.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResetController {

    private final AccountService accountService;

    @Autowired
    public ResetController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/reset")
    public ResponseEntity<String> reset() {
        accountService.reset();
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
