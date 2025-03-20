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
        Stripe.apiKey = stripeApiKey; // Configurar Stripe solo una vez
    }

    @Transactional
    public String createSubscription(User user, String priceId) throws StripeException {
        // 🔹 Verificar si el usuario ya tiene una suscripción activa en la base de datos
        Optional<com.isppG8.infantem.infantem.subscription.Subscription> existingSubscription = subscriptionRepository
                .findByUserAndActiveTrue(user);
        if (existingSubscription.isPresent()) {
            throw new IllegalStateException("El usuario ya tiene una suscripción activa.");
        }

        // 🔹 Buscar o crear un cliente en Stripe
        String customerId = findOrCreateStripeCustomer(user);

        // 🔹 Crear suscripción en Stripe
        SubscriptionCreateParams subscriptionParams = SubscriptionCreateParams.builder().setCustomer(customerId)
                .addItem(SubscriptionCreateParams.Item.builder().setPrice(priceId).build()).build();

        Subscription stripeSubscription = Subscription.create(subscriptionParams);

        // 🔹 Guardar la suscripción en la base de datos
        com.isppG8.infantem.infantem.subscription.Subscription newSubscription = new com.isppG8.infantem.infantem.subscription.Subscription();
        newSubscription.setUser(user);
        newSubscription.setStripeSubscriptionId(stripeSubscription.getId());
        newSubscription.setStripeCustomerId(customerId); // ⚠ Asegúrate de que existe este campo en la entidad
        newSubscription.setStartDate(LocalDate.now());
        newSubscription.setActive(true);

        subscriptionRepository.save(newSubscription);
        return stripeSubscription.getId();
    }

    private String findOrCreateStripeCustomer(User user) throws StripeException {
        Optional<com.isppG8.infantem.infantem.subscription.Subscription> existingSubscription = subscriptionRepository
                .findByUser(user);

        if (existingSubscription.isPresent() && existingSubscription.get().getStripeCustomerId() != null) {
            return existingSubscription.get().getStripeCustomerId();
        }

        // Crear nuevo cliente en Stripe
        CustomerCreateParams customerParams = CustomerCreateParams.builder().setEmail(user.getEmail())
                .setName(user.getName()).build();

        Customer stripeCustomer = Customer.create(customerParams);
        return stripeCustomer.getId();
    }
}
