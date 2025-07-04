package com.example.cardservice.mapper;

import com.example.cardservice.dto.ConfirmRequest;
import com.example.cardservice.dto.ConfirmResponse;
import com.example.cardservice.dto.TransferRequest;
import com.example.cardservice.dto.TransferResponse;
import com.example.cardservice.model.Operation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TransferMapper {

    public Operation toOperation(TransferRequest request) {
        Operation operation = new Operation();
        operation.setOperationId(UUID.randomUUID().toString());
        operation.setCardFromNumber(request.cardFromNumber());
        operation.setCardToNumber(request.cardToNumber());
        operation.setAmount(BigDecimal.valueOf(request.amount().value()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
        operation.setCurrency(request.amount().currency());
        operation.setCommission(operation.getAmount().multiply(new BigDecimal("0.01")).setScale(2, BigDecimal.ROUND_HALF_UP));
        operation.setDateTime(LocalDateTime.now());
        operation.setStatus("PENDING");
        operation.setConfirmationCode("0000"); // Фиксированный код для отладки
        return operation;
    }

    public TransferResponse toTransferResponse(Operation operation) {
        return new TransferResponse(operation.getOperationId());
    }

    public ConfirmResponse toConfirmResponse(Operation operation) {
        return new ConfirmResponse(operation.getOperationId());
    }
}