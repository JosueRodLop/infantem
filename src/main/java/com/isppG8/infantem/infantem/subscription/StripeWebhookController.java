package com.isppG8.infantem.infantem.subscription;

import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    private static final String STRIPE_WEBHOOK_SECRET = "whsec_tu_clave_secreta";

    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signatureHeader) {
        try {
            Event event = Webhook.constructEvent(payload, signatureHeader, STRIPE_WEBHOOK_SECRET);

            switch (event.getType()) {
                case "checkout.session.completed":
                    // Lógica para completar el pago y activar suscripción
                    System.out.println("Sesión de pago completada: " + event.getId());
                    break;
                case "invoice.payment_succeeded":
                    System.out.println("Pago exitoso para la suscripción.");
                    break;
                case "customer.subscription.deleted":
                    System.out.println("Suscripción cancelada.");
                    break;
                default:
                    System.out.println("Evento no manejado: " + event.getType());
            }

            return ResponseEntity.ok("Evento recibido correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error en la validación del Webhook: " + e.getMessage());
        }
    }
}
