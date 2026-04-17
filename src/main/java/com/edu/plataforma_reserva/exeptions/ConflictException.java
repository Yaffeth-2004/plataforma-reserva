package com.edu.plataforma_reserva.exeptions;


public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
