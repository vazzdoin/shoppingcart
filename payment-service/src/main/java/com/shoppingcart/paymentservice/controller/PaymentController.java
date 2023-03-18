package com.shoppingcart.paymentservice.controller;

import com.shoppingcart.paymentservice.model.PaymentRequest;
import com.shoppingcart.paymentservice.model.PaymentResponse;
import com.shoppingcart.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest request) {
        return new ResponseEntity<>(paymentService.doPayment(request), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentDetails(@PathVariable long orderId) {
        return new ResponseEntity<>(paymentService.getPaymentDetailsByOrder(orderId),
                HttpStatus.OK);
    }
}
