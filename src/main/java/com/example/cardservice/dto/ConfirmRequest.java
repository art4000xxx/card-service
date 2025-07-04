package com.example.cardservice.dto;

public record ConfirmRequest(
        String operationId,
        String code
) {}