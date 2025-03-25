package com.ebanx.techtest.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = DepositEventDto.class, name = "deposit"),
            @JsonSubTypes.Type(value = WithdrawEventDto.class, name = "withdraw"),
            @JsonSubTypes.Type(value = TransferEventDto.class, name = "transfer")
    })
    public abstract class EventDto {
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

