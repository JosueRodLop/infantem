package com.isppG8.infantem.infantem.subscription;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/paypal-webhooks")
public class PayPalWebhookController {

    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody(required = false) String payload) {
        System.out.println("Webhook recibido: " + payload);
        return ResponseEntity.ok("Webhook recibido correctamente");
    }
}
