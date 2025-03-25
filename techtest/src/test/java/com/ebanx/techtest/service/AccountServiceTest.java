package com.ebanx.techtest.service;

import com.ebanx.techtest.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService();
    }

    @Test
    void reset_shouldClearAllAccounts() {
        // Given
        accountService.deposit("100", 10);
        accountService.deposit("200", 20);

        // When
        accountService.reset();

        // Then
        Optional<Account> account100 = accountService.getAccount("100");
        Optional<Account> account200 = accountService.getAccount("200");

        assertTrue(account100.isEmpty());
        assertTrue(account200.isEmpty());
    }

    @Test
    void getAccount_shouldReturnEmptyOptional_whenAccountDoesNotExist() {
        // When
        Optional<Account> account = accountService.getAccount("nonexistent");

        // Then
        assertTrue(account.isEmpty());
    }

    @Test
    void getAccount_shouldReturnAccount_whenAccountExists() {
        // Given
        accountService.deposit("100", 10);

        // When
        Optional<Account> account = accountService.getAccount("100");

        // Then
        assertTrue(account.isPresent());
        assertEquals("100", account.get().getId());
        assertEquals(10, account.get().getBalance());
    }

    @Test
    void deposit_shouldCreateNewAccount_whenAccountDoesNotExist() {
        // When
        Account account = accountService.deposit("100", 10);

        // Then
        assertEquals("100", account.getId());
        assertEquals(10, account.getBalance());

        // Verify account was stored
        Optional<Account> storedAccount = accountService.getAccount("100");
        assertTrue(storedAccount.isPresent());
        assertEquals(10, storedAccount.get().getBalance());
    }

    @Test
    void deposit_shouldAddToExistingBalance_whenAccountExists() {
        // Given
        accountService.deposit("100", 10);

        // When
        Account account = accountService.deposit("100", 15);

        // Then
        assertEquals("100", account.getId());
        assertEquals(25, account.getBalance());

        // Verify account was updated
        Optional<Account> storedAccount = accountService.getAccount("100");
        assertTrue(storedAccount.isPresent());
        assertEquals(25, storedAccount.get().getBalance());
    }

    @Test
    void withdraw_shouldReturnEmptyOptional_whenAccountDoesNotExist() {
        // When
        Optional<Account> account = accountService.withdraw("nonexistent", 10);

        // Then
        assertTrue(account.isEmpty());
    }

    @Test
    void withdraw_shouldReduceBalance_whenAccountExists() {
        // Given
        accountService.deposit("100", 30);

        // When
        Optional<Account> account = accountService.withdraw("100", 10);

        // Then
        assertTrue(account.isPresent());
        assertEquals("100", account.get().getId());
        assertEquals(20, account.get().getBalance());

        // Verify account was updated
        Optional<Account> storedAccount = accountService.getAccount("100");
        assertTrue(storedAccount.isPresent());
        assertEquals(20, storedAccount.get().getBalance());
    }

    @Test
    void transfer_shouldReturnEmptyOptional_whenOriginAccountDoesNotExist() {
        // When
        Optional<Map<String, Account>> result = accountService.transfer("nonexistent", "200", 10);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void transfer_shouldCreateDestinationAccount_whenItDoesNotExist() {
        // Given
        accountService.deposit("100", 30);

        // When
        Optional<Map<String, Account>> result = accountService.transfer("100", "200", 10);

        // Then
        assertTrue(result.isPresent());
        Map<String, Account> accounts = result.get();

        assertEquals(20, accounts.get("origin").getBalance());
        assertEquals(10, accounts.get("destination").getBalance());

        // Verify accounts were updated/created
        Optional<Account> originAccount = accountService.getAccount("100");
        Optional<Account> destAccount = accountService.getAccount("200");

        assertTrue(originAccount.isPresent());
        assertTrue(destAccount.isPresent());
        assertEquals(20, originAccount.get().getBalance());
        assertEquals(10, destAccount.get().getBalance());
    }

    @Test
    void transfer_shouldUpdateBothAccounts_whenBothExist() {
        // Given
        accountService.deposit("100", 30);
        accountService.deposit("200", 5);

        // When
        Optional<Map<String, Account>> result = accountService.transfer("100", "200", 10);

        // Then
        assertTrue(result.isPresent());
        Map<String, Account> accounts = result.get();

        assertEquals(20, accounts.get("origin").getBalance());
        assertEquals(15, accounts.get("destination").getBalance());

        // Verify accounts were updated
        Optional<Account> originAccount = accountService.getAccount("100");
        Optional<Account> destAccount = accountService.getAccount("200");

        assertTrue(originAccount.isPresent());
        assertTrue(destAccount.isPresent());
        assertEquals(20, originAccount.get().getBalance());
        assertEquals(15, destAccount.get().getBalance());
    }
}
