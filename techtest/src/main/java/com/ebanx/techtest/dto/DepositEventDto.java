package com.ebanx.techtest.dto;

public class DepositEventDto extends EventDto{
    private String destination;
    private int amount;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
