package com.isppG8.infantem.infantem.subscription;

import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.param.SubscriptionCreateParams;
import com.isppG8.infantem.infantem.user.User;  // Importamos tu modelo User
import com.isppG8.infantem.infantem.payment.PaymentService;
import com.isppG8.infantem.infantem.payment.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Autowired
    private PaymentService paymentService;

    // 🔹 Crear suscripción para un usuario existente en tu base de datos
    public String createSubscription(User user, String priceId) throws StripeException {
        // Buscamos el método de pago de Stripe del usuario
        Optional<Payment> paymentOpt = paymentService.getPaymentByUserId((long)user.getId());

        if (paymentOpt.isEmpty()) {
            throw new IllegalArgumentException("El usuario no tiene un método de pago asociado a Stripe.");
        }

        String customerId = paymentOpt.get().getStripeCustomerId();

        // Configuración de los parámetros de la suscripción
        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                .setCustomer(customerId)  // Usamos el ID de Stripe del cliente vinculado al usuario
                .addItem(
                        SubscriptionCreateParams.Item.builder()
                                .setPrice(priceId)  // Este es el ID del precio en Stripe
                                .build()
                )
                .build();

        // Creamos la suscripción en Stripe
        Subscription subscription = Subscription.create(params);
        return subscription.getId();
    }

    public void cancelSubscription(String subscriptionId) throws StripeException {
        Subscription subscription = Subscription.retrieve(subscriptionId);
        subscription.cancel();
    }
}
