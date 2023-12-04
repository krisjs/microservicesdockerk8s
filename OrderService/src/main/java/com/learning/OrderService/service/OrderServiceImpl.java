package com.learning.OrderService.service;

import com.learning.OrderService.entity.Order;
import com.learning.OrderService.exception.CustomException;
import com.learning.OrderService.external.client.PaymentService;
import com.learning.OrderService.external.client.ProductService;
import com.learning.OrderService.external.request.PaymentRequest;
import com.learning.OrderService.external.response.PaymentResponse;

import com.learning.OrderService.model.OrderRequest;
import com.learning.OrderService.model.OrderResponse;
import com.learning.OrderService.repository.OrderRepository;
import com.learning.ProductService.model.ProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public long placeOrder(OrderRequest orderRequest) {

        log.info("Placing Order Request: {}", orderRequest);
        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());
        log.info("Creating order with status created");
        Order order = Order.builder()
                        .amount(orderRequest.getTotalAmount())
                                .orderStatus("CREATED")
                                        .productId((orderRequest.getProductId()))
                                                .orderDate(Instant.now())
                                                        .quantity(orderRequest.getQuantity())
                                                                .build();

        order = orderRepository.save(order);

        log.info("Calling payment service to complete the payment");
        log.info("Order places successfully with Order id: {}", order.getId());

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();
        String orderStatus = null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done successfully. Changing the order status to placed");
            orderStatus = "PLACED";
        }catch (Exception e){
            log.error("Error occusred in payment. Changing order status to Failed");
            orderStatus = "PAYMENT_FAILED";
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order detauils for Order ID: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found for the order id: " + orderId,
                        "NOT_FOUND", 404));

        log.info("Invoking product service to fetch product for id: {}",order.getProductId());
        ProductResponse productResponse =
                restTemplate.getForObject(
                        "http://PRODUCT-SERVICE/product/"+order.getProductId(),
                        ProductResponse.class
                );
        log.info("Getting payment information from the payment service");
        PaymentResponse paymentResponse =
                restTemplate.getForObject(
                        "http://PAYMENT-SERVICE/payment/order/"+ order.getId(),
                        PaymentResponse.class
                );
        OrderResponse.ProductDetails productDetails =
        OrderResponse.ProductDetails
                .builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .build();

        OrderResponse.PaymentDetails paymentDetails =
                OrderResponse.PaymentDetails
                        .builder()
                        .paymentId(paymentResponse.getPaymentId())
                        .paymentStatus(paymentResponse.getStatus())
                        .paymentDate(paymentResponse.getPaymentDate())
                        .paymentMode(paymentResponse.getPaymentMode())
                        .build();

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();

        return orderResponse;
    }
}
