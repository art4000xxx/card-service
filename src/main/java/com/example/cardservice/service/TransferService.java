package com.example.cardservice.service;

import com.example.cardservice.dto.ConfirmRequest;
import com.example.cardservice.dto.ConfirmResponse;
import com.example.cardservice.dto.TransferRequest;
import com.example.cardservice.dto.TransferResponse;
import com.example.cardservice.model.Card;
import com.example.cardservice.model.Operation;
import com.example.cardservice.repository.CardRepository;
import com.example.cardservice.repository.OperationRepository;
import com.example.cardservice.mapper.TransferMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {
    private static final DateTimeFormatter LOG_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final CardRepository cardRepository;
    private final OperationRepository operationRepository;
    private final TransferMapper transferMapper;

    public TransferResponse transfer(TransferRequest request) {
        Card cardFrom = cardRepository.findByNumber(request.cardFromNumber())
                .orElseThrow(() -> new IllegalArgumentException("Card not found: " + request.cardFromNumber()));

        Card cardTo = cardRepository.findByNumber(request.cardToNumber())
                .orElseThrow(() -> new IllegalArgumentException("Card not found: " + request.cardToNumber()));

        if (!isValidCard(cardFrom, request)) {
            throw new IllegalArgumentException("Invalid card details");
        }

        BigDecimal totalAmount = calculateTotalAmount(request);
        if (cardFrom.getBalance().compareTo(totalAmount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        Operation operation = transferMapper.toOperation(request);
        operationRepository.save(operation);
        log.info("Date: {}, Time: {}, From: {}, To: {}, Amount: {}, Commission: {}, Status: PENDING",
                operation.getDateTime().format(LOG_DATE_FORMATTER),
                operation.getDateTime().format(LOG_DATE_FORMATTER),
                request.cardFromNumber(),
                request.cardToNumber(),
                operation.getAmount(),
                operation.getCommission());

        return transferMapper.toTransferResponse(operation);
    }

    public ConfirmResponse confirmOperation(ConfirmRequest request) {
        Operation operation = operationRepository.findById(request.operationId())
                .orElseThrow(() -> new IllegalArgumentException("Operation not found: " + request.operationId()));

        if (!request.code().equals(operation.getConfirmationCode())) {
            throw new IllegalArgumentException("Invalid confirmation code");
        }

        Card cardFrom = cardRepository.findByNumber(operation.getCardFromNumber()).orElseThrow();
        Card cardTo = cardRepository.findByNumber(operation.getCardToNumber()).orElseThrow();

        BigDecimal totalAmount = operation.getAmount().add(operation.getCommission());
        cardFrom.setBalance(cardFrom.getBalance().subtract(totalAmount));
        cardTo.setBalance(cardTo.getBalance().add(operation.getAmount()));

        cardRepository.save(cardFrom);
        cardRepository.save(cardTo);

        operation.setStatus("COMPLETED");
        operation.setDateTime(LocalDateTime.now());
        operationRepository.save(operation);
        log.info("Date: {}, Time: {}, From: {}, To: {}, Amount: {}, Commission: {}, Status: COMPLETED",
                operation.getDateTime().format(LOG_DATE_FORMATTER),
                operation.getDateTime().format(LOG_DATE_FORMATTER),
                operation.getCardFromNumber(),
                operation.getCardToNumber(),
                operation.getAmount(),
                operation.getCommission());

        return transferMapper.toConfirmResponse(operation);
    }

    private boolean isValidCard(Card card, TransferRequest request) {
        return card.getCardNumber().equals(request.cardFromNumber()) &&
                card.getValidTill().equals(request.cardFromValidTill()) &&
                card.getCvv().equals(request.cardFromCVV());
    }

    private BigDecimal calculateTotalAmount(TransferRequest request) {
        BigDecimal amount = BigDecimal.valueOf(request.amount().value()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal commission = amount.multiply(new BigDecimal("0.01")).setScale(2, BigDecimal.ROUND_HALF_UP);
        return amount.add(commission);
    }
}