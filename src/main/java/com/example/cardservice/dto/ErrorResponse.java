package com.example.cardservice.dto;

public record ErrorResponse(
        String message,
        int id
) {}