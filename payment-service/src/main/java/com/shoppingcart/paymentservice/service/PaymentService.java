package com.shoppingcart.paymentservice.service;

import com.shoppingcart.paymentservice.model.PaymentRequest;
import com.shoppingcart.paymentservice.model.PaymentResponse;

public interface PaymentService {
    long doPayment(PaymentRequest request);

    PaymentResponse getPaymentDetailsByOrder(long orderId);
}
