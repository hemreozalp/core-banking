package com.example.core_banking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCustomerRequest(
        @NotBlank(message = "Customer name cannot be empty")
        String firstName,

        @NotBlank(message = "Customer surname cannot be empty")
        String lastName,

        @NotBlank(message = "TCKN cannot be empty")
        @Size(min = 11, max = 11, message = "TCKN must be 11 digits long.")
        String identityNumber,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid email format")
        String email
) {
}
