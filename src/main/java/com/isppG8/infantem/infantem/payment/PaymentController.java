package com.isppG8.infantem.infantem.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/{userId}")
    public ResponseEntity<String> getStripeCustomerId(@PathVariable Long userId) {
        Optional<Payment> payment = paymentService.getPaymentByUserId(userId);
        return payment.map(p -> ResponseEntity.ok(p.getStripeCustomerId()))
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
}
