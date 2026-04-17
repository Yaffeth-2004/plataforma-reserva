package com.edu.plataforma_reserva.exeptions;


public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
