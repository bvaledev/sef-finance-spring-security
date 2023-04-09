package br.dev.brendo.secfinance.dto;

import java.time.LocalDateTime;

public record ErrorViewDTO(
        Integer status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp
) {
}
