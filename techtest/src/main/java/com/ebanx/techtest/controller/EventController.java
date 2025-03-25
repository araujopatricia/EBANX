package com.ebanx.techtest.controller;

import com.ebanx.techtest.dto.*;
import com.ebanx.techtest.model.Account;
import com.ebanx.techtest.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class EventController {

    private final AccountService accountService;

    @Autowired
    public EventController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/event")
    public ResponseEntity<?> handleEvent(@RequestBody EventDto event) {
        if (event instanceof DepositEventDto) {
            return handleDeposit((DepositEventDto) event);
        } else if (event instanceof WithdrawEventDto) {
            return handleWithdraw((WithdrawEventDto) event);
        } else if (event instanceof TransferEventDto) {
            return handleTransfer((TransferEventDto) event);
        }

        return ResponseEntity.badRequest().build();
    }

    private ResponseEntity<ResponseDto> handleDeposit(DepositEventDto deposit) {
        Account account = accountService.deposit(deposit.getDestination(), deposit.getAmount());
        ResponseDto responseDto = ResponseDto.fromDeposit(account);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    private ResponseEntity<?> handleWithdraw(WithdrawEventDto withdraw) {
        Optional<Account> accountOpt = accountService.withdraw(withdraw.getOrigin(), withdraw.getAmount());

        if (accountOpt.isPresent()) {
            ResponseDto responseDto = ResponseDto.fromWithdraw(accountOpt.get());
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<?> handleTransfer(TransferEventDto transfer) {
        Optional<Map<String, Account>> accountsOpt = accountService.transfer(
                transfer.getOrigin(),
                transfer.getDestination(),
                transfer.getAmount()
        );

        if (accountsOpt.isPresent()) {
            Map<String, Account> accounts = accountsOpt.get();
            Account origin = accounts.get("origin");
            Account destination = accounts.get("destination");
            ResponseDto responseDto = ResponseDto.fromTransfer(origin, destination);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
        }
    }
}