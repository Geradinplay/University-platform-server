package com.example.exception;

// Ошибка валидации данных (400)
public class UserAlreadyExistsException extends BaseException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
