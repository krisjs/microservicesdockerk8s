package com.learning.OrderService.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private String errorCode;
    private int status;

    public CustomException(String message, String errorCode, int status){
        super(message);
        this.errorCode = errorCode;
        this.status = status;

    }
}
