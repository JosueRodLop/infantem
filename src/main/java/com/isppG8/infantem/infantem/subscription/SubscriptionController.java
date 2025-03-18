package com.isppG8.infantem.infantem.subscription;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscription(@PathVariable Long id) {
        Optional<Subscription> subscription = subscriptionService.findById(id);
        return subscription.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription) {
        Subscription newSubscription = subscriptionService.create(subscription);
        return ResponseEntity.ok(newSubscription);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subscription> updateSubscription(@PathVariable Long id, @RequestBody Subscription subscription) {
        Subscription updatedSubscription = subscriptionService.update(id, subscription);
        return ResponseEntity.ok(updatedSubscription);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        subscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para iniciar una suscripción y obtener la URL de aprobación
    @PostMapping("/start/{planId}")
    public ResponseEntity<String> startSubscription(@PathVariable String planId) {
        try {
            String approvalUrl = subscriptionService.createSubscriptionPlan(planId);
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
        return subscriptionService.createSubscriptionPlan(agreementId);
    }

    @PostMapping("/cancel/{agreementId}")
    public String cancelSubscription(@PathVariable String agreementId) throws PayPalRESTException {
        return subscriptionService.cancelSubscription(agreementId);
    }
}    
