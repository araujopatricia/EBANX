package com.ebanx.techtest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ebanx.techtest.model.Account;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {
    private AccountDto origin;
    private AccountDto destination;

    // Construtor padrão necessário para Jackson
    public ResponseDto() {
    }

    public static ResponseDto fromDeposit(Account account) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setDestination(new AccountDto(account));
        return responseDto;
    }

    public static ResponseDto fromWithdraw(Account account) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setOrigin(new AccountDto(account));
        return responseDto;
    }

    public static ResponseDto fromTransfer(Account origin, Account destination) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setOrigin(new AccountDto(origin));
        responseDto.setDestination(new AccountDto(destination));
        return responseDto;
    }

    public AccountDto getOrigin() {
        return origin;
    }

    public void setOrigin(AccountDto origin) {
        this.origin = origin;
    }

    public AccountDto getDestination() {
        return destination;
    }

    public void setDestination(AccountDto destination) {
        this.destination = destination;
    }
}