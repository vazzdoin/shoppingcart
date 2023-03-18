package com.shoppingcart.orderservice.service;

import com.shoppingcart.orderservice.model.OrderRequest;
import com.shoppingcart.orderservice.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderById(long orderId);
}
