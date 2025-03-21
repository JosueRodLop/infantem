package com.isppG8.infantem.infantem.stripe;

import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stripe.model.Subscription;

@RestController
@RequestMapping("/stripe/webhook")
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private final StripeService stripeService;

    public StripeWebhookController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) throws StripeException {
        if (sigHeader == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falta la firma del webhook");
        }

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            switch (event.getType()) {
                case "checkout.session.completed":
                    stripeService.handleCheckoutSessionCompleted(event);
                    break;
                case "invoice.payment_succeeded":
                    stripeService.handleInvoicePaymentSucceeded(event);
                    break;
                case "customer.subscription.deleted":
                    stripeService.handleSubscriptionCanceled(event);
                    break;
                default:
                    System.out.println("Evento no manejado: " + event.getType());
            }
            return ResponseEntity.ok("Evento recibido correctamente");

        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Firma inválida del webhook");
        } catch (JsonSyntaxException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payload inválido");
        }
    }

    @PostMapping
    public String manejarEventoStripe(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Stripe.apiKey = "TU_SECRET_KEY";

        try {
            Event evento = Webhook.constructEvent(payload, sigHeader, endpointSecret
            );

            if ("customer.subscription.created".equals(evento.getType())) {
                Subscription sub = (Subscription) evento.getDataObjectDeserializer().getObject().orElse(null);
                if (sub != null) {
                    String stripeSubscriptionId = sub.getId();
                    String customerId = sub.getCustomer();

                    // Aquí actualizarías tu entidad subscriptionInfantem
                    System.out.println("Suscripción creada: " + stripeSubscriptionId + " para el cliente: " + customerId);
                }
            }

            return "Evento procesado";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error procesando el evento";
        }
    }
}
