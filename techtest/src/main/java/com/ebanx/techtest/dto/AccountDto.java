package com.ebanx.techtest.dto;

import com.ebanx.techtest.model.Account;

public class AccountDto {
    private String id;
    private int balance;

    // Construtor padrão necessário para Jackson
    public AccountDto() {
    }

    public AccountDto(Account account) {
        this.id = account.getId();
        this.balance = account.getBalance();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}