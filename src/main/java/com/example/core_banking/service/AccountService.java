package com.example.core_banking.service;

import com.example.core_banking.dto.CreateAccountRequest;
import com.example.core_banking.entity.Account;
import com.example.core_banking.entity.Customer;
import com.example.core_banking.exception.AccountNotFoundException;
import com.example.core_banking.exception.CustomerNotFoundException;
import com.example.core_banking.repository.AccountRepository;
import com.example.core_banking.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public Account createAccount(CreateAccountRequest request) {

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found!"));

        Account account = new Account();
        account.setCustomer(customer);
        account.setCurrency(request.currency().toUpperCase());
        account.setBalance(BigDecimal.ZERO);
        account.setIban(generateUniqieIban());

        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Account getAccountByIdWithSecurity(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + " ID'li hesap bulunamadı!"));

        // Token (In-Memory'deki username)
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        boolean isCustomer = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"));

        if (isCustomer) {
            String dbIdentityNumber = account.getCustomer().getIdentityNumber();

            if (!dbIdentityNumber.equals(currentUsername)) {
                throw new AccessDeniedException("Bu hesaba erişim yetkiniz bulunmamaktadır!");
            }
        }

        return account;
    }

    private String generateUniqieIban() {
        Random random = new Random();
        String iban;
        do {
            long number = (long) (random.nextDouble() * 100_000_000_000_000L);
            iban = "TR" + String.format("%014d", number);
        } while (accountRepository.existsByIban(iban)); // If iban created the same, create something new

        return iban;
    }
}
