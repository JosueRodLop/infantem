package com.isppG8.infantem.infantem.subscription;

import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.SubscriptionCreateParams;
import com.isppG8.infantem.infantem.payment.Payment;
import com.isppG8.infantem.infantem.payment.PaymentService;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserService;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.model.Event;

import java.util.Optional;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService; // Para actualizar el estado de suscripción en la BD

    @Autowired
    private SubscriptionInfantemService subscriptionService; // Para manejar la suscripción en la BD

    // 🔹 Crear suscripción para un usuario existente en tu base de datos
    public String createSubscription(User user, String priceId) throws StripeException {
        // Buscamos el método de pago de Stripe del usuario
        Optional<Payment> paymentOpt = paymentService.getPaymentByUserId((long) user.getId());

        if (paymentOpt.isEmpty()) {
            throw new IllegalArgumentException("El usuario no tiene un método de pago asociado a Stripe.");
        }

        String customerId = paymentOpt.get().getStripeCustomerId();

        // Configuración de los parámetros de la suscripción
        SubscriptionCreateParams params = SubscriptionCreateParams.builder().setCustomer(customerId) // Usamos el ID de
                                                                                                     // Stripe del
                                                                                                     // cliente
                                                                                                     // vinculado al
                                                                                                     // usuario
                .addItem(SubscriptionCreateParams.Item.builder().setPrice(priceId) // Este es el ID del precio en Stripe
                        .build())
                .build();

        // Creamos la suscripción en Stripe
        Subscription subscription = Subscription.create(params);
        return subscription.getId();
    }

    public void cancelSubscription(String subscriptionId) throws StripeException {
        Subscription subscription = Subscription.retrieve(subscriptionId);
        subscription.cancel();
    }

    // 🔹 Manejar cuando un usuario completa un pago exitoso en Stripe
    public void handleCheckoutSessionCompleted(Event event) throws StripeException {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (!dataObjectDeserializer.getObject().isPresent()) return;

        Session session = (Session) dataObjectDeserializer.getObject().get();
        String customerId = session.getCustomer();
        String subscriptionId = session.getSubscription();

        if (customerId == null || subscriptionId == null) return;

        // Buscar usuario en la BD basado en el customerId de Stripe
        Optional<User> userOpt = userService.getUserByStripeCustomerId(customerId);
        if (userOpt.isEmpty()) return;

        User user = userOpt.get();
        subscriptionService.activateSubscription(user, subscriptionId);
    }

    // 🔹 Manejar cuando se paga correctamente una factura de suscripción
    public void handleInvoicePaymentSucceeded(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (!dataObjectDeserializer.getObject().isPresent()) return;

        Invoice invoice = (Invoice) dataObjectDeserializer.getObject().get();
        String subscriptionId = invoice.getSubscription();

        if (subscriptionId == null) return;

        // Marcar la suscripción como activa en la BD
        subscriptionService.updateSubscriptionStatus(subscriptionId, true);
    }

    // 🔹 Manejar cuando una suscripción es cancelada
    public void handleSubscriptionCanceled(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (!dataObjectDeserializer.getObject().isPresent()) return;

        Subscription subscription = (Subscription) dataObjectDeserializer.getObject().get();
        String subscriptionId = subscription.getId();

        if (subscriptionId == null) return;

        // Marcar la suscripción como cancelada en la BD
        subscriptionService.updateSubscriptionStatus(subscriptionId, false);
    }
}
