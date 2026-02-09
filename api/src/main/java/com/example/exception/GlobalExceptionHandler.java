package com.example.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Ловим ошибки уникальности (UserAlreadyExistsException) -> 400 Bad Request
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserExists(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Ловим нарушения целостности данных (race condition при одновременной регистрации) -> 400 Bad Request
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = ex.getMessage();
        if (message != null && message.toLowerCase().contains("username")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username is already taken");
        } else if (message != null && message.toLowerCase().contains("email")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email is already in use");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("This user data already exists");
    }

    // Ловим ошибки валидации (неверные данные в Request Body) -> 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation error: " + errors);
    }

    // Ловим ошибки входа (AuthException) -> 401 Unauthorized
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> handleAuthError(AuthException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    // Ловим вообще всё остальное, чтобы не упасть с 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobal(Exception ex) {
        // В продакшене лучше не логировать ex.getMessage() напрямую клиенту для 500-х ошибок
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred on the server");
    }

}

