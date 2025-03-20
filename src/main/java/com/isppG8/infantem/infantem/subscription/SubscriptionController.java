package com.isppG8.infantem.infantem.subscription;

import com.isppG8.infantem.infantem.payment.Payment;
import com.isppG8.infantem.infantem.payment.PaymentService;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;

    public SubscriptionController(SubscriptionService subscriptionService, UserService userService) {
        this.subscriptionService = subscriptionService;
        this.userService = userService;
    }

    @PostMapping("/start/{userId}/{priceId}")
    public ResponseEntity<String> startSubscription(@PathVariable Long userId, @PathVariable String priceId) {
        User user = userService.getUserById(userId);

        try {
            String subscriptionId = subscriptionService.createSubscription(user, priceId);
            return ResponseEntity.ok("Suscripción creada con éxito. ID: " + subscriptionId);
        } catch (StripeException | IllegalStateException e) {
            return ResponseEntity.badRequest().body("Error al crear suscripción: " + e.getMessage());
        }
    }
}
