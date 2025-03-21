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
public class SubscriptionInfantemService {

    @Value("${stripe.secret.key}")
    private String stripeApiKey;

    private final SubscriptionInfantemRepository subscriptionInfantemRepository;

    public SubscriptionInfantemService(SubscriptionInfantemRepository subscriptionRepository) {
        this.subscriptionInfantemRepository = subscriptionRepository;
    }

    @Transactional
    public String createSubscription(User user, String priceId) throws StripeException {
        Stripe.apiKey = stripeApiKey; // Configurar Stripe en cada transacción si es necesario

        Optional<SubscriptionInfantem> existingSubscription = subscriptionInfantemRepository.findByUserAndActiveTrue(user);
        if (existingSubscription.isPresent()) {
            throw new IllegalStateException("El usuario ya tiene una suscripción activa.");
        }

        String customerId = findOrCreateStripeCustomer(user);

        SubscriptionCreateParams subscriptionParams = SubscriptionCreateParams.builder()
                .setCustomer(customerId)
                .addItem(SubscriptionCreateParams.Item.builder().setPrice(priceId).build())
                .build();

        Subscription stripeSubscription = Subscription.create(subscriptionParams);

        SubscriptionInfantem newSubscription = new SubscriptionInfantem();
        newSubscription.setUser(user);
        newSubscription.setStripeSubscriptionId(stripeSubscription.getId());
        newSubscription.setStripeCustomerId(customerId);
        newSubscription.setStartDate(LocalDate.now());
        newSubscription.setActive(true);

        subscriptionInfantemRepository.save(newSubscription);
        return stripeSubscription.getId();
    }

    private String findOrCreateStripeCustomer(User user) throws StripeException {
        Optional<SubscriptionInfantem> existingSubscription = subscriptionInfantemRepository.findByUser(user);

        if (existingSubscription.isPresent() && existingSubscription.get().getStripeCustomerId() != null) {
            return existingSubscription.get().getStripeCustomerId();
        }

        CustomerCreateParams customerParams = CustomerCreateParams.builder()
                .setEmail(user.getEmail())
                .setName(user.getName())
                .build();

        Customer stripeCustomer = Customer.create(customerParams);
        return stripeCustomer.getId();
    }

    public void activateSubscription(User user, String subscriptionId) {
        Optional<SubscriptionInfantem> subOpt = subscriptionInfantemRepository.findByUser(user);
    
        if (subOpt.isPresent()) {
            SubscriptionInfantem subscription = subOpt.get();
            subscription.setStripeSubscriptionId(subscriptionId);
            subscription.setActive(true);
            subscriptionInfantemRepository.save(subscription);
        }
    }
    
    public void updateSubscriptionStatus(String stripeSubscriptionId, boolean isActive) {
        Optional<SubscriptionInfantem> subOpt = subscriptionInfantemRepository.findByStripeSubscriptionId(stripeSubscriptionId);
    
        if (subOpt.isPresent()) {
            SubscriptionInfantem subscription = subOpt.get();
            subscription.setActive(isActive);
            subscriptionInfantemRepository.save(subscription);
        }
    }
}
