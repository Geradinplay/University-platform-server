package com.example.exception;

// Ошибка авторизации (401)
public class AuthException extends BaseException {
    public AuthException(String message) {
        super(message);
    }
}
