package com.ebanx.techtest.controller;


import com.ebanx.techtest.model.Account;
import com.ebanx.techtest.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private BalanceController balanceController;

    @Test
    void getBalance_shouldReturn404AndZero_whenAccountDoesNotExist() {
        // Given
        String accountId = "nonexistent";
        when(accountService.getAccount(accountId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<Integer> response = balanceController.getBalance(accountId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(0, response.getBody());
    }

    @Test
    void getBalance_shouldReturn200AndBalance_whenAccountExists() {
        // Given
        String accountId = "100";
        Account account = new Account(accountId, 25);
        when(accountService.getAccount(accountId)).thenReturn(Optional.of(account));

        // When
        ResponseEntity<Integer> response = balanceController.getBalance(accountId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(25, response.getBody());
    }
}
