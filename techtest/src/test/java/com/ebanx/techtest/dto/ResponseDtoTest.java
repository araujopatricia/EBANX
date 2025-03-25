package com.ebanx.techtest.dto;

import com.ebanx.techtest.model.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseDtoTest {

    @Test
    void fromDeposit_shouldCreateResponseWithDestinationAccount() {
        // Given
        Account account = new Account("100", 10);

        // When
        ResponseDto responseDto = ResponseDto.fromDeposit(account);

        // Then
        assertNull(responseDto.getOrigin());
        assertNotNull(responseDto.getDestination());
        assertEquals("100", responseDto.getDestination().getId());
        assertEquals(10, responseDto.getDestination().getBalance());
    }

    @Test
    void fromWithdraw_shouldCreateResponseWithOriginAccount() {
        // Given
        Account account = new Account("100", 20);

        // When
        ResponseDto responseDto = ResponseDto.fromWithdraw(account);

        // Then
        assertNotNull(responseDto.getOrigin());
        assertNull(responseDto.getDestination());
        assertEquals("100", responseDto.getOrigin().getId());
        assertEquals(20, responseDto.getOrigin().getBalance());
    }

    @Test
    void fromTransfer_shouldCreateResponseWithBothAccounts() {
        // Given
        Account origin = new Account("100", 10);
        Account destination = new Account("200", 20);

        // When
        ResponseDto responseDto = ResponseDto.fromTransfer(origin, destination);

        // Then
        assertNotNull(responseDto.getOrigin());
        assertNotNull(responseDto.getDestination());
        assertEquals("100", responseDto.getOrigin().getId());
        assertEquals(10, responseDto.getOrigin().getBalance());
        assertEquals("200", responseDto.getDestination().getId());
        assertEquals(20, responseDto.getDestination().getBalance());
    }
}
