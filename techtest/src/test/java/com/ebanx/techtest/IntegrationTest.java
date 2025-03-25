package com.ebanx.techtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        // Reset the state before each test
        mockMvc.perform(post("/reset"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBalanceForNonExistingAccount() throws Exception {
        mockMvc.perform(get("/balance?account_id=1234"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("0"));
    }

    @Test
    void shouldCreateAccountWithInitialBalance() throws Exception {
        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":\"deposit\", \"destination\":\"100\", \"amount\":10}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"destination\": {\"id\":\"100\", \"balance\":10}}"));
    }

    @Test
    void shouldDepositIntoExistingAccount() throws Exception {
        // First create the account
        mockMvc.perform(post("/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"deposit\", \"destination\":\"100\", \"amount\":10}"));

        // Then deposit again
        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":\"deposit\", \"destination\":\"100\", \"amount\":10}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"destination\": {\"id\":\"100\", \"balance\":20}}"));
    }

    @Test
    void shouldGetBalanceForExistingAccount() throws Exception {
        // First create the account
        mockMvc.perform(post("/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"deposit\", \"destination\":\"100\", \"amount\":20}"));

        // Then get balance
        mockMvc.perform(get("/balance?account_id=100"))
                .andExpect(status().isOk())
                .andExpect(content().string("20"));
    }

    @Test
    void shouldReturnNotFoundForWithdrawFromNonExistingAccount() throws Exception {
        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":\"withdraw\", \"origin\":\"200\", \"amount\":10}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("0"));
    }

    @Test
    void shouldWithdrawFromExistingAccount() throws Exception {
        // First create the account
        mockMvc.perform(post("/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"deposit\", \"destination\":\"100\", \"amount\":20}"));

        // Then withdraw
        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":\"withdraw\", \"origin\":\"100\", \"amount\":5}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"origin\": {\"id\":\"100\", \"balance\":15}}"));
    }

    @Test
    void shouldTransferFromExistingAccount() throws Exception {
        // First create the account
        mockMvc.perform(post("/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"deposit\", \"destination\":\"100\", \"amount\":15}"));

        // Then transfer
        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":\"transfer\", \"origin\":\"100\", \"amount\":15, \"destination\":\"300\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"origin\": {\"id\":\"100\", \"balance\":0}, \"destination\": {\"id\":\"300\", \"balance\":15}}"));
    }

    @Test
    void shouldReturnNotFoundForTransferFromNonExistingAccount() throws Exception {
        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":\"transfer\", \"origin\":\"200\", \"amount\":15, \"destination\":\"300\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("0"));
    }
}
