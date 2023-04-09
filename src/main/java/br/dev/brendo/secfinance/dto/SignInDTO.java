package br.dev.brendo.secfinance.dto;

import jakarta.validation.constraints.*;

public record SignInDTO(
        @NotNull(message = "The email field cannot be null")
        @NotEmpty(message = "The email cannot be empty")
        @Email(message = "The email address is invalid", flags = Pattern.Flag.CASE_INSENSITIVE)
        String email,
        @NotNull(message = "The password cannot be null")
        @NotEmpty(message = "The password cannot be empty")
        @Size(min = 9, max = 20, message = "The field requires a minimum of 9 and a maximum of 20 characters")
        String password) {}
