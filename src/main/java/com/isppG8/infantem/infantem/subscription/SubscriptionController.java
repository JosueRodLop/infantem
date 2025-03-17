package com.isppG8.infantem.infantem.subscription;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.api.payments.Agreement;
import com.paypal.base.rest.PayPalRESTException;

@RestController
@RequestMapping("api/v1/subcriptions")
public class SubscriptionController {
    
    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    public List<Subscription> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions();
    }

    // Endpoint para iniciar una suscripción y obtener la URL de aprobación
    @PostMapping("/start/{planId}")
    public ResponseEntity<String> startSubscription(@PathVariable String planId) {
        try {
            String approvalUrl = subscriptionService.createSubscription(planId);
            return ResponseEntity.ok(approvalUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al iniciar la suscripción: " + e.getMessage());
        }
    }

    // Endpoint para confirmar una suscripción después de la aprobación de PayPal
    @PostMapping("/confirm")
    public ResponseEntity<String> confirmSubscription(@RequestParam String paymentId, @RequestParam String payerId) throws PayPalRESTException {
        Optional<Subscription> subscription = subscriptionService.confirmSubscription(paymentId, payerId);
        return subscription.map(s -> ResponseEntity.ok("Suscripción confirmada con éxito"))
                          .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo confirmar la suscripción"));
    }

    @PostMapping("/create/{agreementId}")
    public String createSubscription(@PathVariable String agreementId) throws MalformedURLException, UnsupportedEncodingException, PayPalRESTException {
        return subscriptionService.createSubscription(agreementId);
    }

    @PostMapping("/cancel/{agreementId}")
    public String cancelSubscription(@PathVariable String agreementId) throws PayPalRESTException {
        return subscriptionService.cancelSubscription(agreementId);
    }
}    
