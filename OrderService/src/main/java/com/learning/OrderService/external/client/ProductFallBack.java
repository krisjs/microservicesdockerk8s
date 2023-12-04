package com.learning.OrderService.external.client;

import com.learning.OrderService.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductFallBack{

    public ResponseEntity<Void> reduceQuantity(long productId, long quantity) {
        throw new CustomException("Payment service is not available","Unavailable",500);
    }
}
