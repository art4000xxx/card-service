package com.example.cardservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private String cardNumber;
    private String validTill;
    private String cvv;
    private BigDecimal balance;
}