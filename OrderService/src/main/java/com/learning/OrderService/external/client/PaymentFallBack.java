package com.learning.OrderService.external.client;

import com.learning.OrderService.exception.CustomException;
import com.learning.OrderService.external.request.PaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
@Component
public class PaymentFallBack{

    public ResponseEntity<Long> doPayment(PaymentRequest paymentRequest) {
        throw new CustomException("Payment service is not available","Unavailable",500);
    }
}
