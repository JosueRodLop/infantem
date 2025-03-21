package com.isppG8.infantem.infantem.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.CustomerCreateParams;
import com.isppG8.infantem.infantem.payment.Payment;
import com.isppG8.infantem.infantem.payment.PaymentService;
import com.isppG8.infantem.infantem.subscription.SubscriptionInfantemService;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserService;
import com.stripe.model.checkout.Session;
import com.stripe.param.SubscriptionCancelParams;

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

    // 🔹 Crear un cliente en Stripe si no existe
    public String createCustomer(User user) throws StripeException {
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(user.getEmail())
                .setName(user.getName())
                .build();
        Customer customer = Customer.create(params);
        return customer.getId();
    }

    // 🔹 Crear suscripción para un usuario existente en tu base de datos
    public String createSubscription(User user, String priceId) throws StripeException {
        Optional<Payment> paymentOpt = paymentService.getPaymentByUserId((long) user.getId());

        String customerId = paymentOpt.map(Payment::getStripeCustomerId)
                .orElseGet(() -> {
                    try {
                        return createCustomer(user);
                    } catch (StripeException e) {
                        throw new RuntimeException("Error creando cliente en Stripe", e);
                    }
                });

        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                .setCustomer(customerId)
                .addItem(SubscriptionCreateParams.Item.builder().setPrice(priceId).build())
                .build();

        Subscription subscription = Subscription.create(params);
        return subscription.getId();
    }

    public void cancelSubscription(String subscriptionId) throws StripeException {
        Subscription subscription = Subscription.retrieve(subscriptionId);
        SubscriptionCancelParams params = SubscriptionCancelParams.builder().build();
        subscription.cancel(params);
    }

    // 🔹 Manejar cuando un usuario completa un pago exitoso en Stripe
    public void handleCheckoutSessionCompleted(Event event) throws StripeException {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (!dataObjectDeserializer.getObject().isPresent()) return;

        Session session = (Session) dataObjectDeserializer.getObject().get();
        String customerId = session.getCustomer();
        String subscriptionId = session.getSubscription();

        if (customerId == null || subscriptionId == null) return;

        Optional<User> userOpt = userService.getUserByStripeCustomerId(customerId);
        userOpt.ifPresent(user -> subscriptionService.activateSubscription(user, subscriptionId));
    }

    // 🔹 Manejar cuando se paga correctamente una factura de suscripción
    public void handleInvoicePaymentSucceeded(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (!dataObjectDeserializer.getObject().isPresent()) return;

        Invoice invoice = (Invoice) dataObjectDeserializer.getObject().get();
        String subscriptionId = invoice.getSubscription();
        if (subscriptionId == null) return;

        subscriptionService.updateSubscriptionStatus(subscriptionId, true);
    }

    // 🔹 Manejar cuando una suscripción es cancelada
    public void handleSubscriptionCanceled(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (!dataObjectDeserializer.getObject().isPresent()) return;

        Subscription subscription = (Subscription) dataObjectDeserializer.getObject().get();
        String subscriptionId = subscription.getId();
        if (subscriptionId == null) return;

        subscriptionService.updateSubscriptionStatus(subscriptionId, false);
    }

    // 🔹 Manejar cuando una suscripción es creada
    public void handleSubscriptionCreated(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (!dataObjectDeserializer.getObject().isPresent()) return;

        Subscription subscription = (Subscription) dataObjectDeserializer.getObject().get();
        String subscriptionId = subscription.getId();
        String customerId = subscription.getCustomer();
        if (subscriptionId == null || customerId == null) return;

        Optional<User> userOpt = userService.getUserByStripeCustomerId(customerId);
        userOpt.ifPresent(user -> subscriptionService.activateSubscription(user, subscriptionId));
    }
}
