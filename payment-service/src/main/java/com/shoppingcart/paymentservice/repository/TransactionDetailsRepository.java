package com.shoppingcart.paymentservice.repository;

import com.shoppingcart.paymentservice.entity.TransactionDetails;
import com.shoppingcart.paymentservice.model.PaymentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Long> {

    TransactionDetails findByOrderId(long orderId);
}
