package com.example.exception;

// Базовое исключение для нашего приложения
public class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }
}

