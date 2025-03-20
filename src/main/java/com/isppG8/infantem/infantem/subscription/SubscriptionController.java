package com.isppG8.infantem.infantem.subscription;

import com.isppG8.infantem.infantem.payment.Payment;
import com.isppG8.infantem.infantem.payment.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/start/{userId}/{priceId}")
    public ResponseEntity<String> startSubscription(@PathVariable Long userId, @PathVariable String priceId) {
        Optional<Payment> payment = paymentService.getPaymentByUserId(userId);

        if (payment.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no tiene método de pago en Stripe");
        }

        try {
            String subscriptionId = subscriptionService.createSubscription(payment.get().getStripeCustomerId(),
                    priceId);
            return ResponseEntity.ok("Suscripción creada con éxito. ID: " + subscriptionId);
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body("Error al crear suscripción: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/{subscriptionId}")
    public ResponseEntity<String> cancelSubscription(@PathVariable String subscriptionId) {
        try {
            subscriptionService.cancelSubscription(subscriptionId);
            return ResponseEntity.ok("Suscripción cancelada con éxito");
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body("Error al cancelar suscripción: " + e.getMessage());
        }
    }
}
