package dev.practice.shopapp.exceptions;

import dev.practice.shopapp.models.ApiResponse;
import dev.practice.shopapp.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse<Object> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                               HttpServletRequest request) {
        return ResponseUtil.error(Collections.singletonList(ex.getMessage()), "Resource not found",
                404, request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }
        return ResponseUtil.error(errors, "Validation error", 400, request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex,
                                                                         HttpServletRequest request) {
        String uri = request.getRequestURI();
        String queryParams = request.getQueryString();
        if (queryParams != null && !queryParams.isBlank()) {
            uri += "?" + queryParams;
        }
        String cleanError = String.format("The parameter '%s' must be of type '%s', but you provided '%s'",
                ex.getName(),
                Objects.requireNonNull(ex.getRequiredType()).getSimpleName(),
                ex.getValue());
        return ResponseUtil.error(
                Collections.singletonList(cleanError),
                "Incorrect request parameters",
                400,
                uri);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
                                                                     HttpServletRequest request) {
        return ResponseUtil.error(Collections.singletonList(ex.getMessage()),
                "Invalid request body: please provide a valid JSON payload",
                400, request.getRequestURI());
    }

    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public org.springframework.http.ResponseEntity<ApiResponse<Object>> handleResponseStatusException(
            org.springframework.web.server.ResponseStatusException ex,
            HttpServletRequest request) {

        // This extracts "Email already in use" from the exception
        String errorMessage = ex.getReason() != null ? ex.getReason() : "Conflict occurred";

        ApiResponse<Object> errorResponse = ResponseUtil.error(
                Collections.singletonList(errorMessage),
                "Business Logic Error", // The general message
                ex.getStatusCode().value(), // This will be 409
                request.getRequestURI()
        );

        return new org.springframework.http.ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Object> handleDataIntegrity(org.springframework.dao.DataIntegrityViolationException ex, HttpServletRequest request) {
        return ResponseUtil.error(Collections.singletonList("Database integrity error: likely a duplicate entry."),
                "Conflict", 409, request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Object> handleGeneralException(Exception ex, HttpServletRequest request) {
        // 1. Log the REAL error for the developer to see in the terminal
        log.error("Unhandled exception occurred: ", ex);

        // 2. Return a SAFE, generic message to the client
        return ResponseUtil.error(
                Collections.singletonList("An unexpected internal error occurred. Please contact support."),
                "Internal server error",
                500,
                request.getRequestURI());
    }
}
