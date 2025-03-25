package com.ebanx.techtest.controller;

import com.ebanx.techtest.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ResetControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private ResetController resetController;

    @Test
    void reset_shouldCallServiceAndReturn200() {
        // Given
        doNothing().when(accountService).reset();

        // When
        ResponseEntity<String> response = resetController.reset();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(accountService).reset();
    }
}
