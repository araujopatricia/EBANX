package com.ebanx.techtest.model;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void constructor_shouldInitializeWithCorrectValues() {
        // When
        Account account = new Account("100", 10);

        // Then
        assertEquals("100", account.getId());
        assertEquals(10, account.getBalance());
    }

    @Test
    void deposit_shouldIncreaseBalance() {
        // Given
        Account account = new Account("100", 10);

        // When
        account.deposit(15);

        // Then
        assertEquals(25, account.getBalance());
    }

    @Test
    void withdraw_shouldDecreaseBalance() {
        // Given
        Account account = new Account("100", 30);

        // When
        account.withdraw(10);

        // Then
        assertEquals(20, account.getBalance());
    }
}
