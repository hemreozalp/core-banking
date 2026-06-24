package com.example.core_banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAccountRequest(
        @NotNull(message = "Customer ID cannot be empty")
        Long customerId,

        @NotBlank(message = "Currency cannot be empty")
        @Size(min = 3, max = 3, message = "The currency name must be 3 characters long (e.g., TRY, USD)")
        String currency
) {
}
