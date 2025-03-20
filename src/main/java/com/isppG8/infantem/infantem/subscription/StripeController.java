package com.isppG8.infantem.infantem.subscription;

import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @Autowired
    private UserService userService;

    // Crear una suscripción para un usuario registrado
    @PostMapping("/create-subscription")
    public ResponseEntity<String> createSubscription(@RequestParam Long userId, @RequestParam String priceId) {
        User userOpt = userService.getUserById(userId);

        try {
            String subscriptionId = stripeService.createSubscription(userOpt, priceId);
            return ResponseEntity.ok("Suscripción creada con ID: " + subscriptionId);
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body("Error al crear suscripción: " + e.getMessage());
        }
    }

    // Cancelar una suscripción basada en su ID de Stripe
    @PostMapping("/cancel-subscription")
    public ResponseEntity<String> cancelSubscription(@RequestParam String subscriptionId) {
        try {
            stripeService.cancelSubscription(subscriptionId);
            return ResponseEntity.ok("Suscripción cancelada correctamente.");
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body("Error al cancelar suscripción: " + e.getMessage());
        }
    }
}
