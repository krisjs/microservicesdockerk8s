package com.learning.OrderService.service;

import com.learning.OrderService.model.OrderRequest;
import com.learning.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
