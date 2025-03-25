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

import jakarta.annotation.PostConstruct;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserService;
import com.stripe.model.checkout.Session;
import com.stripe.model.tax.Registration.CountryOptions.Us;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriptionInfantemService {

    @Value("${stripe.secret.key}")
    private String stripeApiKey;

    @Autowired
    private UserService userService;

    private final SubscriptionInfantemRepository subscriptionInfantemRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

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
    public String createCustomer(String email, String name, String description) throws Exception {
        CustomerCreateParams params = CustomerCreateParams.builder().setEmail(email).setName(name)
                .setDescription(description).build();

        Customer customer = Customer.create(params);
        return customer.getId(); // Devuelve solo el ID del cliente creado en Stripe
    }

    // 3. Asociar método de pago al cliente
    public String attachPaymentMethodToCustomer(String paymentMethodId, String customerId) throws Exception {
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        PaymentMethodAttachParams params = PaymentMethodAttachParams.builder().setCustomer(customerId).build();
        paymentMethod.attach(params);
        return paymentMethod.getId(); // Devuelve solo el ID del método de pago
    }

    // 4. Crear una suscripción
    public SubscriptionInfantem createSubscription(Long userId, String customerId, String priceId,
            String paymentMethodId) throws Exception {
        // Crear los parámetros de la suscripción en Stripe
        SubscriptionCreateParams params = SubscriptionCreateParams.builder().setCustomer(customerId)
                .addItem(SubscriptionCreateParams.Item.builder().setPrice(priceId).build())
                .setDefaultPaymentMethod(paymentMethodId).build();

        // Crear la suscripción en Stripe
        Subscription stripeSubscription = Subscription.create(params);
        User user = userService.getUserById(userId);

        // Crear la suscripción en la base de datos
        SubscriptionInfantem newSubscription = new SubscriptionInfantem();
        newSubscription.setUser(user); // Solo se necesita el ID del usuario
        newSubscription.setStartDate(LocalDate.now());
        newSubscription.setActive(true);
        newSubscription.setStripePaymentMethodId(paymentMethodId);
        newSubscription.setStripeSubscriptionId(stripeSubscription.getId()); // Extrae solo el ID
        newSubscription.setStripeCustomerId(customerId);

        // Guardar en la base de datos
        return subscriptionInfantemRepository.save(newSubscription);
    }

    public SubscriptionInfantem createSubscriptionNew(Long userId, String priceId, String paymentMethodId) throws Exception {
        User user = userService.getUserById(userId);
        String customerId = createCustomer(user.getEmail(), user.getName(), "Cliente creado por Infantem");
        String paymentMethod =  attachPaymentMethodToCustomer(paymentMethodId, customerId);

        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                .setCustomer(customerId)
                .addItem(SubscriptionCreateParams.Item.builder().setPrice(priceId).build())
                .setDefaultPaymentMethod(paymentMethod)
                .build();
    
        // Crear la suscripción en Stripe
        Subscription stripeSubscription = Subscription.create(params);
    
        // Crear la suscripción en la base de datos
        SubscriptionInfantem newSubscription = new SubscriptionInfantem();
        newSubscription.setUser(user); // Solo se necesita el ID del usuario
        newSubscription.setStartDate(LocalDate.now());
        newSubscription.setActive(true);
        newSubscription.setStripePaymentMethodId(paymentMethodId);
        newSubscription.setStripeSubscriptionId(stripeSubscription.getId()); // Extrae solo el ID
        newSubscription.setStripeCustomerId(customerId);
    
        // Guardar en la base de datos
        return subscriptionInfantemRepository.save(newSubscription);
    }
    

    // 5. Cancelar una suscripción
    public Subscription cancelSubscription(String subscriptionId) throws Exception {
        Subscription subscription = Subscription.retrieve(subscriptionId);
        SubscriptionUpdateParams params = SubscriptionUpdateParams.builder().setCancelAtPeriodEnd(true).build();
        return subscription.update(params);
    }

    // 6. Conseguir usuarios por email
    public List<Map<String, Object>> getCustomersByEmail(String email) throws Exception {
        CustomerListParams params = CustomerListParams.builder().setEmail(email).build();
        List<Customer> customers = Customer.list(params).getData();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        return customers.stream().map(customer -> objectMapper.convertValue(customer, Map.class))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getPaymentMethodsByCustomer(String customerId, Integer last4) {
        try {
            PaymentMethodListParams params = PaymentMethodListParams.builder().setCustomer(customerId)
                    .setType(PaymentMethodListParams.Type.CARD).build();

            List<PaymentMethod> paymentMethods = PaymentMethod.list(params).getData();

            for (PaymentMethod paymentMethod : paymentMethods) {
                if (paymentMethod.getCard() != null && paymentMethod.getCard().getLast4() != null
                        && paymentMethod.getCard().getLast4().equals(last4.toString())) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.convertValue(paymentMethod, Map.class);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener métodos de pago: " + e.getMessage());
        }

        return null;
    }

    public void handleCheckoutSessionCompleted(Event event) throws StripeException {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (!dataObjectDeserializer.getObject().isPresent())
            return;

        Session session = (Session) dataObjectDeserializer.getObject().get();
        String customerId = session.getCustomer();
        String subscriptionId = session.getSubscription();

        if (customerId == null || subscriptionId == null)
            return;

        Optional<User> userOpt = userService.getUserByStripeCustomerId(customerId);
        userOpt.ifPresent(user -> activateSubscription(user, subscriptionId));
    }

    // 🔹 Manejar cuando se paga correctamente una factura de suscripción
    public void handleInvoicePaymentSucceeded(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (!dataObjectDeserializer.getObject().isPresent())
            return;

        Invoice invoice = (Invoice) dataObjectDeserializer.getObject().get();
        String subscriptionId = invoice.getSubscription();
        if (subscriptionId == null)
            return;

        updateSubscriptionStatus(subscriptionId, true);
    }

    // 🔹 Manejar cuando una suscripción es cancelada
    public void handleSubscriptionCanceled(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (!dataObjectDeserializer.getObject().isPresent())
            return;

        Subscription subscription = (Subscription) dataObjectDeserializer.getObject().get();
        String subscriptionId = subscription.getId();
        if (subscriptionId == null)
            return;

        updateSubscriptionStatus(subscriptionId, false);
    }

    // 🔹 Manejar cuando una suscripción es creada
    public void handleSubscriptionCreated(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (!dataObjectDeserializer.getObject().isPresent())
            return;

        Subscription subscription = (Subscription) dataObjectDeserializer.getObject().get();
        String subscriptionId = subscription.getId();
        String customerId = subscription.getCustomer();
        if (subscriptionId == null || customerId == null)
            return;

        Optional<User> userOpt = userService.getUserByStripeCustomerId(customerId);
        userOpt.ifPresent(user -> activateSubscription(user, subscriptionId));
    }

    public Optional<SubscriptionInfantem>  getSubscriptionUserById(Long userId) {
        Optional<SubscriptionInfantem> subscription = subscriptionInfantemRepository.findSubscriptionByUserId(userId);
        return subscription;
    }
}
