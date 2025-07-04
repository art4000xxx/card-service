package com.example.cardservice.service;

import com.example.cardservice.dto.ConfirmRequest;
import com.example.cardservice.dto.ConfirmResponse;
import com.example.cardservice.model.Card;
import com.example.cardservice.model.Operation;
import com.example.cardservice.repository.CardRepository;
import com.example.cardservice.repository.OperationRepository;
import com.example.cardservice.mapper.TransferMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private TransferMapper transferMapper;

    @InjectMocks
    private TransferService transferService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void confirmOperation_success_updatesBalancesAndSavesOperation() {
        String opId = "op-123";

        Card fromCard = new Card();
        fromCard.setCardNumber("1111");
        fromCard.setValidTill("12/25");
        fromCard.setCvv("123");
        fromCard.setBalance(new BigDecimal("200.00"));

        Card toCard = new Card();
        toCard.setCardNumber("2222");
        toCard.setValidTill("12/26");
        toCard.setCvv("321");
        toCard.setBalance(new BigDecimal("100.00"));

        Operation operation = new Operation();
        operation.setOperationId(opId);
        operation.setCardFromNumber("1111");
        operation.setCardToNumber("2222");
        operation.setAmount(new BigDecimal("100.00"));
        operation.setCommission(new BigDecimal("1.00"));
        operation.setCurrency("RUB");
        operation.setStatus("PENDING");
        operation.setConfirmationCode("0000");

        ConfirmResponse confirmResponse = new ConfirmResponse(opId);

        when(operationRepository.findById(opId)).thenReturn(Optional.of(operation));
        when(cardRepository.findByNumber("1111")).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByNumber("2222")).thenReturn(Optional.of(toCard));
        when(transferMapper.toConfirmResponse(operation)).thenReturn(confirmResponse);

        ConfirmRequest confirmRequest = new ConfirmRequest(opId, "0000");

        ConfirmResponse response = transferService.confirmOperation(confirmRequest);

        assertEquals(opId, response.operationId());
        assertEquals(new BigDecimal("99.00"), fromCard.getBalance()); // 200 - 100 - 1
        assertEquals(new BigDecimal("200.00"), toCard.getBalance()); // 100 + 100

        verify(cardRepository, times(1)).save(fromCard);
        verify(cardRepository, times(1)).save(toCard);
        verify(operationRepository).save(operation);
        assertEquals("COMPLETED", operation.getStatus());
    }
}