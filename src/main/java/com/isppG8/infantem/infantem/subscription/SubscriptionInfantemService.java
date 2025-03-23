package com.isppG8.infantem.infantem.subscription;

import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.Invoice;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;
import com.stripe.param.PaymentMethodAttachParams;
import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.PaymentMethodListParams;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.SubscriptionUpdateParams;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserService;
import com.stripe.model.checkout.Session;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionInfantemService {

    @Value("${stripe.secret.key}")
    private String stripeApiKey;

    @Autowired
    private UserService userService;

    private final SubscriptionInfantemRepository subscriptionInfantemRepository;

    public SubscriptionInfantemService(SubscriptionInfantemRepository subscriptionRepository) {
        this.subscriptionInfantemRepository = subscriptionRepository;
        Stripe.apiKey = stripeApiKey;
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
        Optional<SubscriptionInfantem> subOpt = subscriptionInfantemRepository
                .findByStripeSubscriptionId(stripeSubscriptionId);

        if (subOpt.isPresent()) {
            SubscriptionInfantem subscription = subOpt.get();
            subscription.setActive(isActive);
            subscriptionInfantemRepository.save(subscription);
        }
    }

    // 1. Crear un cliente en Stripe
    public Customer createCustomer(String email, String name, String description) throws Exception {
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(email)
                .setName(name)
                .setDescription(description)
                .build();
        return Customer.create(params);
    }

    // 2. Crear un método de pago (Tarjeta)
    public PaymentMethod createPaymentMethod(String cardNumber, int expMonth, int expYear, String cvc) throws Exception {
        PaymentMethodCreateParams params = PaymentMethodCreateParams.builder()
                .setType(PaymentMethodCreateParams.Type.CARD)
                .setCard(PaymentMethodCreateParams.CardDetails.builder()
                        .setNumber(cardNumber)
                        .setExpMonth((long) expMonth)
                        .setExpYear((long) expYear)
                        .setCvc(cvc)
                        .build())
                .build();
        return PaymentMethod.create(params);
    }

    // 3. Asociar método de pago al cliente
    public PaymentMethod attachPaymentMethodToCustomer(String paymentMethodId, String customerId) throws Exception {
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        PaymentMethodAttachParams params = PaymentMethodAttachParams.builder()
                .setCustomer(customerId)
                .build();
        return paymentMethod.attach(params);
    }

    // 4. Crear una suscripción
    public Subscription createSubscription(String customerId, String priceId, String paymentMethodId) throws Exception {
        // Crear los parámetros de la suscripción
        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                .setCustomer(customerId)
                .addItem(SubscriptionCreateParams.Item.builder()
                        .setPrice(priceId)
                        .build())
                .setDefaultPaymentMethod(paymentMethodId)
                .build();
    
        // Crear la suscripción en Stripe y obtener el resultado
        Subscription stripeSubscription = Subscription.create(params);
    
        // Obtener el usuario asociado al cliente de Stripe
        User user = userService.getUserByStripeCustomerId(customerId).orElseThrow();
    
        // Crear y guardar la suscripción en la base de datos
        SubscriptionInfantem newSubscription = new SubscriptionInfantem();
        newSubscription.setUser(user);
        newSubscription.setStartDate(LocalDate.now());
        newSubscription.setActive(true);
        newSubscription.setStripePaymentMethodId(paymentMethodId);
        newSubscription.setStripeSubscriptionId(stripeSubscription.getId()); // Aquí obtenemos el ID de la suscripción de Stripe
        newSubscription.setStripeCustomerId(customerId);
    
        subscriptionInfantemRepository.save(newSubscription);
    
        return stripeSubscription; // Devolvemos la suscripción creada en Stripe
    }
    

    // 5. Cancelar una suscripción
    public Subscription cancelSubscription(String subscriptionId) throws Exception {
        Subscription subscription = Subscription.retrieve(subscriptionId);
        SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
                .setCancelAtPeriodEnd(true)
                .build();
        return subscription.update(params);
    }

    // 6. Conseguir usuarios por email
    public List<Customer> getCustomersByEmail(String email) throws Exception {
        CustomerListParams params = CustomerListParams.builder()
                .setEmail(email)
                .build();
        return Customer.list(params).getData();
    }

    // 7. Encontrar método de pago del cliente
    public Boolean getPaymentMethodsByCustomer(String customerId, Integer last4) throws Exception {
        PaymentMethodListParams params = PaymentMethodListParams.builder()
                .setCustomer(customerId)
                .setType(PaymentMethodListParams.Type.CARD)
                .build();
        
        List<PaymentMethod> paymentMethods = PaymentMethod.list(params).getData();
    
        for (PaymentMethod paymentMethod : paymentMethods) {
            if (paymentMethod.getCard() != null && paymentMethod.getCard().getLast4().equals(last4.toString())) {
                return true; // Coincidencia encontrada
            }
        }
    
        return false; // No se encontró coincidencia
    }
    


    public void handleCheckoutSessionCompleted(Event event) throws StripeException {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (!dataObjectDeserializer.getObject().isPresent()) return;

        Session session = (Session) dataObjectDeserializer.getObject().get();
        String customerId = session.getCustomer();
        String subscriptionId = session.getSubscription();

        if (customerId == null || subscriptionId == null) return;

        Optional<User> userOpt = userService.getUserByStripeCustomerId(customerId);
        userOpt.ifPresent(user -> activateSubscription(user, subscriptionId));
    }

    // 🔹 Manejar cuando se paga correctamente una factura de suscripción
    public void handleInvoicePaymentSucceeded(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (!dataObjectDeserializer.getObject().isPresent()) return;

        Invoice invoice = (Invoice) dataObjectDeserializer.getObject().get();
        String subscriptionId = invoice.getSubscription();
        if (subscriptionId == null) return;

        updateSubscriptionStatus(subscriptionId, true);
    }

    // 🔹 Manejar cuando una suscripción es cancelada
    public void handleSubscriptionCanceled(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (!dataObjectDeserializer.getObject().isPresent()) return;

        Subscription subscription = (Subscription) dataObjectDeserializer.getObject().get();
        String subscriptionId = subscription.getId();
        if (subscriptionId == null) return;

        updateSubscriptionStatus(subscriptionId, false);
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
        userOpt.ifPresent(user -> activateSubscription(user, subscriptionId));
    }
}
