package com.edu.plataforma_reserva.exeptions;


public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}