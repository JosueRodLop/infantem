package com.isppG8.infantem.infantem.subscription;

import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionCreateParams;
import com.isppG8.infantem.infantem.user.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Value("${stripe.secret.key}")
    private String stripeApiKey;

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public String createSubscription(User user, String priceId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        // 🔹 Buscar si el usuario ya tiene una suscripción activa
        Optional<Subscription> existingSubscription = subscriptionRepository.findByUserAndActiveTrue(user);

        if (existingSubscription.isPresent()) {
            throw new IllegalStateException("El usuario ya tiene una suscripción activa.");
        }

        // 🔹 Buscar si ya existe un cliente en Stripe para este usuario
        String customerId;
        if (existingSubscription.isEmpty() || existingSubscription.get().getStripeCustomerId() == null) {
            // Si el usuario no tiene cliente en Stripe, lo creamos
            CustomerCreateParams customerParams = CustomerCreateParams.builder().setEmail(user.getEmail())
                    .setName(user.getName()).build();
            Customer stripeCustomer = Customer.create(customerParams);
            customerId = stripeCustomer.getId();
        } else {
            customerId = existingSubscription.get().getStripeCustomerId();
        }

        // 🔹 Crear suscripción en Stripe
        SubscriptionCreateParams subscriptionParams = SubscriptionCreateParams.builder().setCustomer(customerId)
                .addItem(SubscriptionCreateParams.Item.builder().setPrice(priceId).build()).build();
        Subscription stripeSubscription = Subscription.create(subscriptionParams);

        // 🔹 Guardar la suscripción en la base de datos
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setStripeSubscriptionId(stripeSubscription.getId());
        subscription.setStripeCustomerId(customerId);
        subscription.setStartDate(LocalDate.now());
        subscription.setActive(true);

        subscriptionRepository.save(subscription);
        return stripeSubscription.getId();
    }
}
