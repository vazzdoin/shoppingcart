package com.shoppingcart.orderservice.exception;

import lombok.Data;

/*A Custom Exception to handle all custom exception generically. Create more specific exception to
handle different cases. */
@Data
public class CustomException extends RuntimeException {
    private String errorCode;
    private int status;

    public CustomException(String message, String errorCode, int status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}
