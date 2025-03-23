package com.isppG8.infantem.infantem.subscription;

import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Subscription;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionInfantemController {

    @Autowired
    private SubscriptionInfantemService subscriptionService;

    @Autowired
    private UserService userService;

    // Crear una suscripción
    @PostMapping("/create")
    public ResponseEntity<?> createSubscription(@RequestParam String customerId, @RequestParam String priceId,
            @RequestParam String paymentMethodId) {
        try {
            Subscription subscription = subscriptionService.createSubscription(customerId, priceId, paymentMethodId);
            return ResponseEntity.ok(subscription);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear la suscripción: " + e.getMessage());
        }
    }

    // Cancelar una suscripción
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelSubscription(@RequestParam String subscriptionId) {
        try {
            subscriptionService.cancelSubscription(subscriptionId);
            return ResponseEntity.ok("Suscripción cancelada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al cancelar la suscripción: " + e.getMessage());
        }
    }

    // Activar una suscripción
    @PostMapping("/activate")
    public ResponseEntity<?> activateSubscription(@RequestParam String userId, @RequestParam String subscriptionId) {
        try {
            User user = userService.getUserById(Long.parseLong(userId));
            subscriptionService.activateSubscription(user, subscriptionId);
            return ResponseEntity.ok("Suscripción activada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al activar la suscripción: " + e.getMessage());
        }
    }

    // Obtener cliente por email
    @GetMapping("/customers")
    public ResponseEntity<?> getCustomersByEmail(@RequestParam String email, @RequestParam Integer lasts4) {
        try {
            Customer customerFinal = null;
            List<Customer> customers = subscriptionService.getCustomersByEmail(email);
            for (Customer customer : customers) {
                Boolean isCustomere = subscriptionService.getPaymentMethodsByCustomer(customer.getId(), lasts4);
                if (isCustomere) {
                    customerFinal = customer;
                    break;
                }
            }
            return ResponseEntity.ok(customerFinal);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener clientes: " + e.getMessage());
        }
    }

    // Crear un cliente en Stripe
    @PostMapping("/create-customer")
    public ResponseEntity<?> createCustomer(@RequestParam String email, @RequestParam String name,
            @RequestParam String description) {
        try {
            Customer customer = subscriptionService.createCustomer(email, name, description);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear cliente: " + e.getMessage());
        }
    }

    // Crear un método de pago (tarjeta)
    @PostMapping("/create-payment-method")
    public ResponseEntity<?> createPaymentMethod(@RequestParam String cardNumber, @RequestParam int expMonth,
            @RequestParam int expYear, @RequestParam String cvc) {
        try {
            PaymentMethod paymentMethod = subscriptionService.createPaymentMethod(cardNumber, expMonth, expYear, cvc);
            return ResponseEntity.ok(paymentMethod);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear método de pago: " + e.getMessage());
        }
    }

    // Asociar un método de pago a un cliente
    @PostMapping("/attach-payment-method")
    public ResponseEntity<?> attachPaymentMethodToCustomer(@RequestParam String paymentMethodId,
            @RequestParam String customerId) {
        try {
            PaymentMethod attachedPaymentMethod = subscriptionService.attachPaymentMethodToCustomer(paymentMethodId,
                    customerId);
            return ResponseEntity.ok(attachedPaymentMethod);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al asociar método de pago: " + e.getMessage());
        }
    }

    // Actualizar estado de una suscripción
    @PostMapping("/update-status")
    public ResponseEntity<?> updateSubscriptionStatus(@RequestParam String subscriptionId,
            @RequestParam boolean isActive) {
        try {
            subscriptionService.updateSubscriptionStatus(subscriptionId, isActive);
            return ResponseEntity.ok("Estado de la suscripción actualizado.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar el estado: " + e.getMessage());
        }
    }
}
