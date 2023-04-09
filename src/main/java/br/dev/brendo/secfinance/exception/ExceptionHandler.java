package br.dev.brendo.secfinance.exception;

import br.dev.brendo.secfinance.dto.ErrorValidationViewDTO;
import br.dev.brendo.secfinance.dto.ErrorViewDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorValidationViewDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, List<String>> errorList = new HashMap<>();
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        Integer status = HttpStatus.UNPROCESSABLE_ENTITY.value();
        String message = "The form values are invalid";
        String path = request.getServletPath();
        LocalDateTime timestamp = LocalDateTime.now();
        return new ErrorValidationViewDTO(status, errors, message, path, timestamp);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorViewDTO handleNotFoundException(NotFoundException exception, HttpServletRequest request) {
        Integer status = HttpStatus.NOT_FOUND.value();
        String error = HttpStatus.NOT_FOUND.name();
        String message = exception.getMessage();
        String path = request.getServletPath();
        LocalDateTime timestamp = LocalDateTime.now();
        return new ErrorViewDTO(status, error, message, path, timestamp);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorViewDTO globalExceptionHandler(
            Exception exception,
            HttpServletRequest request
    ){
        Integer status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String error = HttpStatus.INTERNAL_SERVER_ERROR.name();
        String message = exception.getMessage();
        String path = request.getServletPath();
        LocalDateTime timestamp = LocalDateTime.now();
        return new ErrorViewDTO(status, error, message, path, timestamp);
    }
}
