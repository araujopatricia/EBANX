package com.ebanx.techtest.controller;


import com.ebanx.techtest.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {

    private final AccountService accountService;

    @Autowired
    public BalanceController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/balance")
    public ResponseEntity<Integer> getBalance(@RequestParam("account_id") String accountId) {
        return accountService.getAccount(accountId)
                .map(account -> new ResponseEntity<>(account.getBalance(), HttpStatus.OK))
                .orElse(new ResponseEntity<>(0, HttpStatus.NOT_FOUND));
    }

}
