package com.isppG8.infantem.infantem.payment;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Optional<Payment> getPaymentByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }
}

