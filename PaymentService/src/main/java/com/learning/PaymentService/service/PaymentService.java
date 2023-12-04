package com.learning.PaymentService.service;

import com.learning.PaymentService.model.PaymentRequest;
import com.learning.PaymentService.model.PaymentResponse;

public interface PaymentService {
    Long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(String orderId);
}
