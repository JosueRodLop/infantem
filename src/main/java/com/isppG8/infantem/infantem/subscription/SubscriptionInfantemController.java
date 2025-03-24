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

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionInfantemController {

    @Autowired
    private SubscriptionInfantemService subscriptionService;

    @Autowired
    private UserService userService;

    // Crear una suscripción
    @PostMapping("/create")
    public ResponseEntity<?> createSubscription(
            @RequestParam String userId, // Ahora se recibe el ID del usuario
            @RequestParam String customerId,
            @RequestParam String priceId,
            @RequestParam String paymentMethodId) {
        try {
            Long id = Long.parseLong(userId); // Convertir el ID a Long
            SubscriptionInfantem subscription = subscriptionService.createSubscription(id, customerId, priceId, paymentMethodId);
            return ResponseEntity.ok(subscription); // Devuelve la SubscriptionInfantem
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


    // Crear un cliente en Stripe
    @PostMapping("/create-customer")
    public ResponseEntity<?> createCustomer(@RequestParam String email, @RequestParam String name,
            @RequestParam String description) {
        try {
            String customerId = subscriptionService.createCustomer(email, name, description);
            Map<String, String> response = new HashMap<>();
            response.put("customerId", customerId); // Envolver el ID en un JSON válido
            return ResponseEntity.ok(response); 
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Error al crear cliente: " + e.getMessage()));
        }
    }



    // Crear un método de pago (tarjeta)
    @PostMapping("/create-payment-method")
    public ResponseEntity<?> createPaymentMethod(@RequestBody Map<String, Object> requestBody) {
        String cardNumber = String.valueOf(requestBody.get("cardNumber"));
        int expMonth = Integer.parseInt(String.valueOf(requestBody.get("expMonth")));
        int expYear = Integer.parseInt(String.valueOf(requestBody.get("expYear")));
        String cvc = String.valueOf(requestBody.get("cvc"));
        String customerId = String.valueOf(requestBody.get("customerId"));
        
        if (expMonth == 3 && expYear == 2002) {
            try {
                String attachedPaymentMethodId = subscriptionService.attachPaymentMethodToCustomer("pm_card_visa", customerId);
                Map<String, String> response = new HashMap<>();
                response.put("paymentMethodId", attachedPaymentMethodId); // Envolver el ID en un JSON válido
                return ResponseEntity.ok(response); 
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error al asociar el método de pago: " + e.getMessage());
            }
        } else {
            try {
                String paymentMethod = subscriptionService.createPaymentMethod(cardNumber, expMonth, expYear, cvc);
                return ResponseEntity.ok(paymentMethod); // Devuelve el ID como un String
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error al crear método de pago: " + e.getMessage());
            }
        }
    }
    

    // Asociar un método de pago a un cliente
    @PostMapping("/attach-payment-method")
    public ResponseEntity<?> attachPaymentMethodToCustomer(@RequestParam String paymentMethodId,
                                                        @RequestParam String customerId) {
        try {
            String attachedPaymentMethodId = subscriptionService.attachPaymentMethodToCustomer(paymentMethodId, customerId);
            return ResponseEntity.ok(attachedPaymentMethodId);  // Devuelve solo el ID del método de pago
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
