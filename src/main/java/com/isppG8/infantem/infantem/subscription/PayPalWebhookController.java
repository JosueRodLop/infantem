package com.isppG8.infantem.infantem.subscription;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/paypal-webhooks")
public class PayPalWebhookController {

    @Autowired
    private SubscriptionService subscriptionService;

    @SuppressWarnings("unchecked")
    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
        try {
            String eventType = (String) payload.get("event_type");
            Map<String, Object> resource = (Map<String, Object>) payload.get("resource");
            String subscriptionId = (String) resource.get("id");

            switch (eventType) {
                case "BILLING.SUBSCRIPTION.EXPIRED":
                    subscriptionService.expireSubscription(subscriptionId);
                    break;
                case "BILLING.SUBSCRIPTION.CANCELLED":
                    subscriptionService.cancelSubscription(subscriptionId);
                    break;
                case "BILLING.SUBSCRIPTION.PAYMENT.FAILED":
                    subscriptionService.handleFailedPayment(subscriptionId);
                    break;
                default:
                    System.out.println("Evento no manejado: " + eventType);
            }
            
            return ResponseEntity.ok("Webhook procesado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el webhook: " + e.getMessage());
        }
    }
}
