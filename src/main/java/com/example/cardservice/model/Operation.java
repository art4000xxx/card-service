package com.example.cardservice.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Operation {
    private String operationId;
    private String cardFromNumber;
    private String cardToNumber;
    private BigDecimal amount;
    private String currency;
    private BigDecimal commission;
    private LocalDateTime dateTime;
    private String status;
    private String confirmationCode;
}