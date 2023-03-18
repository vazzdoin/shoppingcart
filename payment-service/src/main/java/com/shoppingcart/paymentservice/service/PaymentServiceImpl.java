package com.shoppingcart.paymentservice.service;

import com.shoppingcart.paymentservice.entity.TransactionDetails;
import com.shoppingcart.paymentservice.model.PaymentMode;
import com.shoppingcart.paymentservice.model.PaymentRequest;
import com.shoppingcart.paymentservice.model.PaymentResponse;
import com.shoppingcart.paymentservice.repository.TransactionDetailsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Override
    public long doPayment(PaymentRequest request) {
        log.info("Recording Payment Details {}",request);
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .paymentDate(Instant.now())
                .paymentMode(request.getPaymentMode().name())
                .amount(request.getAmount())
                .paymentStatus("SUCCESS")
                .orderId(request.getOrderId())
                .referenceNumber(request.getReferenceNumber())
                .build();

        transactionDetailsRepository.save(transactionDetails);
        log.info("Transaction completed with Id : {}", transactionDetails.getId());
        return transactionDetails.getId();
    }

    @Override
    public PaymentResponse getPaymentDetailsByOrder(long orderId) {
        TransactionDetails transactionDetails =
                transactionDetailsRepository.findByOrderId(Long.valueOf(orderId));
        PaymentResponse paymentResponse
                = PaymentResponse.builder()
                .paymentId(transactionDetails.getId())
                .orderId(transactionDetails.getOrderId())
                .status(transactionDetails.getPaymentStatus())
                .paymentDate(transactionDetails.getPaymentDate())
                .amount(transactionDetails.getAmount())
                .paymentMode(PaymentMode.valueOf(transactionDetails.getPaymentMode()))
                .build();
        return paymentResponse;
    }
}
