package com.isppG8.infantem.infantem.subscription;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.isppG8.infantem.infantem.user.User;
import com.paypal.api.payments.Agreement;
import com.paypal.api.payments.AgreementStateDescriptor;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Plan;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class SubscriptionService {

    private static final int MAX_RETRY_ATTEMPTS = 2;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private APIContext apiContext;

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public Optional<Subscription> findById(Long id) {
        return subscriptionRepository.findById(id);
    }

    public Subscription create(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public Subscription update(Long id, Subscription subscription) {
        Subscription subscriptionToUpdate = subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Suscripción no encontrada"));
        subscriptionToUpdate.setActive(subscription.getActive());
        subscriptionToUpdate.setPrice(subscription.getPrice());
        subscriptionToUpdate.setStartDate(subscription.getStartDate());
        return subscriptionRepository.save(subscription);
    }

    public void delete(Long id) {
        subscriptionRepository.deleteById(id);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Corre todos los días a medianoche
    public void processRenewals() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDate nextMonth = firstDayOfMonth.plusMonths(1);

        List<Subscription> dueSubscriptions = subscriptionRepository.findSubscriptionsExpiringThisMonth(firstDayOfMonth,
                nextMonth);

        for (Subscription subscription : dueSubscriptions) {
            if (subscription.getNextBillingDate().isEqual(today)) { // Validamos que sea el día exacto
                boolean paymentSuccessful = processPayment(subscription);
                if (paymentSuccessful) {
                    subscription.setStartDate(subscription.getNextBillingDate()); // Actualizamos el ciclo
                } else {
                    subscription.setActive(false);
                }
                subscriptionRepository.save(subscription);
            }
        }
    }

    private boolean processPayment(Subscription subscription) {
        try {
            Agreement agreement = new Agreement();
            agreement.setId(subscription.getPayment().getBillingAgreementId());
            agreement = Agreement.execute(apiContext, null);

            return "Active".equalsIgnoreCase(agreement.getState());
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String createSubscriptionPlan(String planId)
            throws PayPalRESTException, MalformedURLException, UnsupportedEncodingException {
        Agreement agreement = new Agreement();
        agreement.setName("Suscripción Mensual");
        agreement.setDescription("Acceso premium a la plataforma");
        agreement.setStartDate("2025-04-01T00:00:00Z");

        Plan plan = new Plan();
        plan.setId(planId);
        agreement.setPlan(plan);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        agreement.setPayer(payer);

        Agreement createdAgreement = agreement.create(apiContext);

        return createdAgreement.getLinks().stream().filter(link -> "approval_url".equals(link.getRel())).findFirst()
                .map(link -> link.getHref())
                .orElseThrow(() -> new RuntimeException("No se encontró la URL de aprobación"));
    }

    public Subscription createSubscription(User user, Double price) {
        Optional<Subscription> existingSubscription = subscriptionRepository.findByUserAndActiveTrue(user);
        if (existingSubscription.isPresent()) {
            throw new IllegalStateException("El usuario ya tiene una suscripción activa.");
        }

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setPrice(price);
        subscription.setStartDate(LocalDate.now());
        subscription.setActive(true);
        return subscriptionRepository.save(subscription);
    }

    public String cancelSubscription(String agreementId) throws PayPalRESTException {
        Agreement agreement = new Agreement();
        agreement.setId(agreementId);
        AgreementStateDescriptor descriptor = new AgreementStateDescriptor();
        descriptor.setNote("Cancelado por el usuario");
        agreement.cancel(apiContext, descriptor);
        return "Suscripción cancelada exitosamente";
    }

    public Optional<Subscription> confirmSubscription(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(apiContext, paymentExecution);

        if ("approved".equalsIgnoreCase(executedPayment.getState())) {
            Subscription subscription = new Subscription();
            subscription.setPrice(4.99); // Precio de ejemplo, se puede parametrizar
            subscription.setStartDate(LocalDate.now());
            subscription.setActive(true);
            return Optional.of(subscriptionRepository.save(subscription));
        }
        return Optional.empty();
    }

    public void expireSubscription(String subscriptionId) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(Long.parseLong(subscriptionId));
        if (subscriptionOpt.isPresent()) {
            Subscription subscription = subscriptionOpt.get();
            subscription.setActive(false);
            subscriptionRepository.save(subscription);
            System.out.println("Suscripción expirada: " + subscriptionId);
        }
    }

    public void handleFailedPayment(String subscriptionId) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(Long.parseLong(subscriptionId));
        if (subscriptionOpt.isPresent()) {
            Subscription subscription = subscriptionOpt.get();
            int retryCount = 0;
            boolean paymentSuccess = false;

            while (retryCount < MAX_RETRY_ATTEMPTS && !paymentSuccess) {
                paymentSuccess = processPayment(subscription);
                retryCount++;
                if (!paymentSuccess) {
                    System.out.println("Reintento de pago " + retryCount + " para la suscripción: " + subscriptionId);
                }
            }

            if (!paymentSuccess) {
                subscription.setActive(false);
                subscriptionRepository.save(subscription);
                System.out.println("Pago fallido tras reintentos, suscripción desactivada: " + subscriptionId);
            }
        }
    }

}
