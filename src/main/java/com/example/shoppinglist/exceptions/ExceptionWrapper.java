package com.example.shoppinglist.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс обертка над Exception
 */
@Data
@AllArgsConstructor
public class ExceptionWrapper extends RuntimeException {
    private String message;

    @Override
    public String toString() {
        return "{ \"message\": " + message + "}";
    }
}
