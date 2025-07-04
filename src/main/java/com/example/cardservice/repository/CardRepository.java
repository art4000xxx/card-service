package com.example.cardservice.repository;

import com.example.cardservice.model.Card;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class CardRepository {
    private final Map<String, Card> cards = new HashMap<>();

    @PostConstruct
    public void init() {
        Card card1 = new Card();
        card1.setCardNumber("1234567890123456");
        card1.setValidTill("12/30");
        card1.setCvv("123");
        card1.setBalance(new BigDecimal("1000000.00"));
        cards.put(card1.getCardNumber(), card1);

        Card card2 = new Card();
        card2.setCardNumber("9876543210987654");
        card2.setValidTill("12/30");
        card2.setCvv("456");
        card2.setBalance(new BigDecimal("500000.00"));
        cards.put(card2.getCardNumber(), card2);
    }

    public Optional<Card> findByNumber(String cardNumber) {
        return Optional.ofNullable(cards.get(cardNumber));
    }

    public void save(Card card) {
        cards.put(card.getCardNumber(), card); // Обновление существующей карты
    }
}