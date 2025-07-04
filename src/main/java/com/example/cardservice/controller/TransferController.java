package com.example.cardservice.controller;

import com.example.cardservice.dto.ConfirmRequest;
import com.example.cardservice.dto.ConfirmResponse;
import com.example.cardservice.dto.TransferRequest;
import com.example.cardservice.dto.TransferResponse;
import com.example.cardservice.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest request) {
        return ResponseEntity.ok(transferService.transfer(request));
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<ConfirmResponse> confirmOperation(@RequestBody ConfirmRequest request) {
        return ResponseEntity.ok(transferService.confirmOperation(request));
    }
}