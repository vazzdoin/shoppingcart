package com.shoppingcart.orderservice.model;

import com.shoppingcart.orderservice.external.request.PaymentRequest;
import com.shoppingcart.orderservice.external.response.PaymentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    private long orderId;
    private Instant orderDate;
    private String orderStatus;
    private long amount;
    private ProductResponse productResponse;
    private PaymentResponse paymentResponse;
}
