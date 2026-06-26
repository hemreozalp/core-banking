package com.example.core_banking.controller;

import com.example.core_banking.dto.CreateAccountRequest;
import com.example.core_banking.entity.Account;
import com.example.core_banking.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        Account createdAccount = accountService.createAccount(request);

        return ResponseEntity.ok(createdAccount);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TELLER', 'AUDITOR', 'CUSTOMER')")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        Account account = accountService.getAccountByIdWithSecurity(id);
        return ResponseEntity.ok(account);
    }
}
