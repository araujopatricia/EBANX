package com.ebanx.techtest.dto;

import com.ebanx.techtest.model.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountDtoTest {

    @Test
    void constructor_shouldCreateDtoFromAccount() {
        // Given
        Account account = new Account("100", 25);

        // When
        AccountDto accountDto = new AccountDto(account);

        // Then
        assertEquals("100", accountDto.getId());
        assertEquals(25, accountDto.getBalance());
    }
}
