package com.learning.OrderService.controller;

import com.learning.OrderService.model.OrderRequest;
import com.learning.OrderService.model.OrderResponse;
import com.learning.OrderService.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasAuthority('Customer')")
    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest){

        long orderId = orderService.placeOrder(orderRequest);
        log.info("Order id is: {}", orderId);

        return new ResponseEntity<>(orderId, HttpStatus.OK);

    }

    @PreAuthorize(("hasAuthority('Admin') || hasAuthority('Customer')"))
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable long orderId){

        OrderResponse orderResponse = orderService.getOrderDetails(orderId);
        return  new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }
}
