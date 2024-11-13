package ru.kata.spring.boot_security.demo.util;

public class UserNotAddedException extends RuntimeException {
    public UserNotAddedException(String message) {
        super(message);
    }
}
