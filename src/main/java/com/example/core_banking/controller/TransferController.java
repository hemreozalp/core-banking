package com.example.core_banking.controller;

import com.example.core_banking.dto.MoneyTransferRequest;
import com.example.core_banking.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<String> transferMoney(@Valid @RequestBody MoneyTransferRequest request) {
        transferService.transferMoney(request);

        return ResponseEntity.ok("Para transferi başarıyla tamamlandı.");
    }
}
