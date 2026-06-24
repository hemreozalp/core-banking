package com.example.core_banking.service;

import com.example.core_banking.dto.MoneyTransferRequest;
import com.example.core_banking.entity.Account;
import com.example.core_banking.entity.Transaction;
import com.example.core_banking.repository.AccountRepository;
import com.example.core_banking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void transferMoney(MoneyTransferRequest request) {

        if (request.sourceIban().equals(request.targetIban())) {
            throw new RuntimeException("Aynı hesaba transfer yapamazsınız!");
        }

        Account sourceAccount = accountRepository.findByIbanWithLock(request.sourceIban())
                .orElseThrow(() -> new RuntimeException("Kaynak hesap bulunamadı"));

        Account targetAccount = accountRepository.findByIbanWithLock(request.targetIban())
                .orElseThrow(() -> new RuntimeException("Alıcı hesap bulunamadı!"));

        if (!sourceAccount.getCurrency().equals(targetAccount.getCurrency())) {
            throw new RuntimeException("Farklı para birimleri arasında transfer henüz desteklenmiyor!");
        }

        if (sourceAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new RuntimeException("Yetersiz bakiye!");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.amount()));
        targetAccount.setBalance(targetAccount.getBalance().add(request.amount()));

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        // Dekont
        Transaction transaction = new Transaction();
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transaction.setAmount(request.amount());
        transaction.setCurrency(sourceAccount.getCurrency());
        transaction.setDescription(request.description());
        transaction.setStatus("SUCCESS");

        transactionRepository.save(transaction);
    }
}
