package br.dev.brendo.secfinance.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorValidationViewDTO(
        Integer status,
        List<String> errors,
        String message,
        String path,
        LocalDateTime timestamp
) {
}
