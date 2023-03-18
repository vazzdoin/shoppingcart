package com.shoppingcart.orderservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import com.shoppingcart.orderservice.entity.Order;
import com.shoppingcart.orderservice.exception.CustomException;
import com.shoppingcart.orderservice.external.client.PaymentServiceFeign;
import com.shoppingcart.orderservice.external.client.ProductServiceFeign;
import com.shoppingcart.orderservice.external.request.PaymentRequest;
import com.shoppingcart.orderservice.external.response.PaymentResponse;
import com.shoppingcart.orderservice.model.OrderRequest;
import com.shoppingcart.orderservice.model.OrderResponse;
import com.shoppingcart.orderservice.model.ProductResponse;
import com.shoppingcart.orderservice.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private ProductServiceFeign productServiceFeign;


    @Autowired
    private PaymentServiceFeign paymentServiceFeign;
    @Override
    public long placeOrder(OrderRequest orderRequest) {
        // Order Entity -> Save the data with Status Order created
        // Product Service ->  Block Products(reduce the quantity)
        // Payment Service -> Payment -> Success -> Complete else cancelled.

        log.info("Placing Order Request: {}", orderRequest);

        productServiceFeign.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());
        log.info("Quantity {} reduced from the product ", orderRequest.getQuantity());
        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .quantity(orderRequest.getQuantity())
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .orderStatus("CREATED")
                .build();

        order = orderRepository.save(order);

        log.info("Calling Payment Service to complete the Payment");

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getOrderId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(order.getAmount())
                .build();

        String orderStatus = null;
        try {
            paymentServiceFeign.doPayment(paymentRequest);
            log.info("Payment Done Successfully. CHnaging the order status to placed");
            orderStatus = "PLACED";
        } catch (Exception e) {
            log.error("Error occured in Payment. Setting the Order Status to FAILED");
            orderStatus = "FAILED";
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        log.info("Order placed successfully with Order Id: {}  ",order.getOrderId());

        return order.getOrderId();
    }

    @Override
    public OrderResponse getOrderById(long orderId) {
        log.info("Get Order details for the Order id: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order Not found", "ORDER_NOT_FOUND", 404));
        log.info("Order details received, converting to the DTO");
        OrderResponse orderResponse = OrderResponse.builder()
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .orderId(order.getOrderId())
                .build();

        log.info("Fetching the Product Detail for the Product Id : {}", order.getProductId());
        ProductResponse productResponse = null;
        try {
            productResponse = productServiceFeign.getProducts(order.getProductId());
        } catch (Exception e) {
            productResponse = null;
            log.error("No associated product found for Product Id : {} ",order.getProductId());
        }
        log.info("Product Details response received : {}", productResponse);
        orderResponse.setProductResponse(productResponse);

        log.info("Fetching the Payment details for Order Id : "+order.getOrderId());
        PaymentResponse paymentResponse = null;
        try {
            log.info("Order Id for Payment search : "+orderId);
            paymentResponse = paymentServiceFeign.getPaymentDetails(orderId).getBody();
//            paymentResponse = restTemplate.getForObject("http://localhost:8081/payment/"+order.getOrderId(), PaymentResponse.class);
            log.info("Payment response : "+paymentResponse);
        } catch (Exception e) {
            paymentResponse = null;
            log.error("No Payment found for the Order Id : "+orderId);
        }

        log.info("Payment details processed for the Order id {}", orderId);

        orderResponse.setPaymentResponse(paymentResponse);
        log.info("Sending back the orderResponse : {}", orderResponse);

        return orderResponse;
    }
}
