package com.ebanx.techtest.controller;

import com.ebanx.techtest.dto.*;
import com.ebanx.techtest.model.Account;
import com.ebanx.techtest.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private EventController eventController;

    @Test
    void handleEvent_shouldHandleDeposit() {
        // Given
        DepositEventDto depositEvent = new DepositEventDto();
        depositEvent.setType("deposit");
        depositEvent.setDestination("100");
        depositEvent.setAmount(10);

        Account account = new Account("100", 10);
        when(accountService.deposit("100", 10)).thenReturn(account);

        // When
        ResponseEntity<?> response = eventController.handleEvent(depositEvent);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ResponseDto responseDto = (ResponseDto) response.getBody();
        assertEquals("100", responseDto.getDestination().getId());
        assertEquals(10, responseDto.getDestination().getBalance());
    }

    @Test
    void handleEvent_shouldHandleWithdrawForExistingAccount() {
        // Given
        WithdrawEventDto withdrawEvent = new WithdrawEventDto();
        withdrawEvent.setType("withdraw");
        withdrawEvent.setOrigin("100");
        withdrawEvent.setAmount(5);

        Account account = new Account("100", 15);
        when(accountService.withdraw("100", 5)).thenReturn(Optional.of(account));

        // When
        ResponseEntity<?> response = eventController.handleEvent(withdrawEvent);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ResponseDto responseDto = (ResponseDto) response.getBody();
        assertEquals("100", responseDto.getOrigin().getId());
        assertEquals(15, responseDto.getOrigin().getBalance());
    }

    @Test
    void handleEvent_shouldReturn404ForWithdrawFromNonExistingAccount() {
        // Given
        WithdrawEventDto withdrawEvent = new WithdrawEventDto();
        withdrawEvent.setType("withdraw");
        withdrawEvent.setOrigin("nonexistent");
        withdrawEvent.setAmount(5);

        when(accountService.withdraw("nonexistent", 5)).thenReturn(Optional.empty());

        // When
        ResponseEntity<?> response = eventController.handleEvent(withdrawEvent);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(0, response.getBody());
    }

    @Test
    void handleEvent_shouldHandleTransferForExistingAccounts() {
        // Given
        TransferEventDto transferEvent = new TransferEventDto();
        transferEvent.setType("transfer");
        transferEvent.setOrigin("100");
        transferEvent.setDestination("200");
        transferEvent.setAmount(15);

        Account originAccount = new Account("100", 5);
        Account destAccount = new Account("200", 15);

        Map<String, Account> accounts = new HashMap<>();
        accounts.put("origin", originAccount);
        accounts.put("destination", destAccount);

        when(accountService.transfer("100", "200", 15)).thenReturn(Optional.of(accounts));

        // When
        ResponseEntity<?> response = eventController.handleEvent(transferEvent);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ResponseDto responseDto = (ResponseDto) response.getBody();
        assertEquals("100", responseDto.getOrigin().getId());
        assertEquals(5, responseDto.getOrigin().getBalance());
        assertEquals("200", responseDto.getDestination().getId());
        assertEquals(15, responseDto.getDestination().getBalance());
    }

    @Test
    void handleEvent_shouldReturn404ForTransferFromNonExistingAccount() {
        // Given
        TransferEventDto transferEvent = new TransferEventDto();
        transferEvent.setType("transfer");
        transferEvent.setOrigin("nonexistent");
        transferEvent.setDestination("200");
        transferEvent.setAmount(15);

        when(accountService.transfer("nonexistent", "200", 15)).thenReturn(Optional.empty());

        // When
        ResponseEntity<?> response = eventController.handleEvent(transferEvent);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(0, response.getBody());
    }
}
