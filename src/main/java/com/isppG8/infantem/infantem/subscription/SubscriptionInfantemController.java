package com.isppG8.infantem.infantem.subscription;

import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionInfantemController {

    @Autowired
    private SubscriptionInfantemService subscriptionService;

    @Autowired
    private UserService userService;

    // Crear una suscripción
    @PostMapping("/create")
    public ResponseEntity<?> createSubscription(@RequestParam String userId, // Ahora se recibe el ID del usuario
            @RequestParam String customerId, @RequestParam String priceId, @RequestParam String paymentMethodId) {
        try {
            Long id = Long.parseLong(userId); // Convertir el ID a Long
            SubscriptionInfantem subscription = subscriptionService.createSubscription(id, customerId, priceId,
                    paymentMethodId);
            return ResponseEntity.ok(subscription); // Devuelve la SubscriptionInfantem
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear la suscripción: " + e.getMessage());
        }
    }

    @PostMapping("/create/new")
    public ResponseEntity<?> createSubscriptionNew(
            @RequestParam String userId,
            @RequestParam String priceId,
            @RequestParam String paymentMethodId) {
        try {
            Long id = Long.parseLong(userId); // Convertir el ID a Long
            SubscriptionInfantem subscription = subscriptionService.createSubscriptionNew(id, priceId, paymentMethodId);
            return ResponseEntity.ok(subscription); // Devuelve la SubscriptionInfantem
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear la suscripción: " + e.getMessage());
        }
    }

    // Obtener cliente por email
    @GetMapping("/customers")
    public ResponseEntity<?> getCustomersByEmail(@RequestParam String email, @RequestParam Integer lasts4) {
        try {
            Map<String, Object> customerFinal = null;
            List<Map<String, Object>> customers = subscriptionService.getCustomersByEmail(email);

            for (Map<String, Object> customer : customers) {
                String customerId = (String) customer.get("id");
                Map<String, Object> paymentMethod = subscriptionService.getPaymentMethodsByCustomer(customerId, lasts4);

                if (paymentMethod != null) {
                    customerFinal = new HashMap<>(customer);
                    customerFinal.put("paymentMethod", paymentMethod);
                    break;
                }
            }

            return ResponseEntity.ok(customerFinal != null ? customerFinal : "Cliente no encontrado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener clientes: " + e.getMessage());
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

    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getSubscription(@PathVariable Long id){
        Optional<SubscriptionInfantem> subscriptionUser = subscriptionService.getSubscriptionUserById(id);
        if(subscriptionUser.isPresent()){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.ok(false);
        }
    }
}
