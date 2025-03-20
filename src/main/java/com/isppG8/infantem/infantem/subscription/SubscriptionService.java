package com.isppG8.infantem.infantem.subscription;
import com.stripe.model.Subscription;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    @Value("${stripe.secret.key}")
    private String stripeApiKey;

    public String createSubscription(String customerId, String priceId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
            .setCustomer(customerId)
            .addItem(SubscriptionCreateParams.Item.builder().setPrice(priceId).build())
            .build();

        Subscription subscription = Subscription.create(params);
        return subscription.getId();
    }

    public void cancelSubscription(String subscriptionId) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        Subscription subscription = Subscription.retrieve(subscriptionId);
        subscription.cancel();
    }
}
