package com.example.cardservice.dto;

public record TransferRequest(
        String cardFromNumber,
        String cardFromValidTill,
        String cardFromCVV,
        String cardToNumber,
        Amount amount
) {
    public record Amount(
            int value,
            String currency
    ) {}
}