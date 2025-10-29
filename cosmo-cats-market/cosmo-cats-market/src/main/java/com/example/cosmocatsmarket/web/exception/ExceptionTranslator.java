package com.example.cosmocatsmarket.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest req) {
        log.warn("Validation failed: {}", ex.getMessage());

        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid input data");
        problem.setType(URI.create("https://errors.cosmo-cats.market/validation-error"));
        problem.setTitle("Validation Error");
        problem.setInstance(URI.create(req.getRequestURI()));

        List<FieldErrorDto> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new FieldErrorDto(err.getField(), err.getDefaultMessage()))
                .collect(Collectors.toList());

        problem.setProperty("path", req.getRequestURI());
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("errors", fieldErrors);

        return problem;
    }


    @ExceptionHandler(ProductNotFoundException.class)
    ProblemDetail handleNotFound(ProductNotFoundException ex, HttpServletRequest req) {
        log.info("Product not found: {}", ex.getMessage());
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setType(URI.create("https://errors.cosmo-cats.market/not-found"));
        problem.setTitle("Product Not Found");
        problem.setInstance(URI.create(req.getRequestURI()));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(ConflictException.class)
    ProblemDetail handleConflict(ConflictException ex, HttpServletRequest req) {
        log.warn("Conflict: {}", ex.getMessage());
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setType(URI.create("https://errors.cosmo-cats.market/conflict"));
        problem.setTitle("Conflict");
        problem.setInstance(URI.create(req.getRequestURI()));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail handleGenericException(Exception ex, HttpServletRequest req) {
        log.error("Unexpected error: ", ex);
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error");
        problem.setType(URI.create("https://errors.cosmo-cats.market/internal-error"));
        problem.setTitle("Internal Server Error");
        problem.setInstance(URI.create(req.getRequestURI()));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    record FieldErrorDto(String field, String message) {}
}
