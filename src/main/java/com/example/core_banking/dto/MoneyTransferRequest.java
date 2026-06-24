package com.example.core_banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MoneyTransferRequest(
        @NotBlank(message = "Kaynak IBAN boş olamaz")
        String sourceIban,

        @NotBlank(message = "Alıcı IBAN boş olamaz")
        String targetIban,

        @NotNull(message = "Tutar boş olamaz")
        @Positive(message = "Transfer tutarı sıfırdan büyük olmalıdır")
        BigDecimal amount,

        String description
) {
}
