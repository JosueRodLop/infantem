package com.isppG8.infantem.infantem.subscription;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.isppG8.infantem.infantem.user.User;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.stripe.exception.StripeException;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionCreateParams;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SubscriptionInfantemServiceTest {

    @InjectMocks
    private SubscriptionInfantemService subscriptionService;

    @Mock
    private SubscriptionInfantemRepository subscriptionRepository;

    @Mock
    private Customer mockCustomer;

    @Mock
    private Subscription mockSubscription;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
    }

    @Test
    void testCreateSubscription_Success() throws StripeException {
        // Simular que el usuario no tiene suscripción activa
        when(subscriptionRepository.findByUserAndActiveTrue(testUser)).thenReturn(Optional.empty());

        // Simular creación de cliente en Stripe
        when(mockCustomer.getId()).thenReturn("cus_123456");
        when(Customer.create(any(CustomerCreateParams.class))).thenReturn(mockCustomer);

        // Simular creación de suscripción en Stripe
        when(mockSubscription.getId()).thenReturn("sub_123456");
        when(Subscription.create(any(SubscriptionCreateParams.class))).thenReturn(mockSubscription);

        // Ejecutar el método a probar
        String subscriptionId = subscriptionService.createSubscription(testUser, "price_ABC");

        // Verificar resultados
        assertNotNull(subscriptionId);
        assertEquals("sub_123456", subscriptionId);
        verify(subscriptionRepository, times(1)).save(any(SubscriptionInfantem.class));
    }

    @Test
    void testCreateSubscription_UserAlreadySubscribed() {
        SubscriptionInfantem existingSubscription = new SubscriptionInfantem();
        existingSubscription.setActive(true);

        when(subscriptionRepository.findByUserAndActiveTrue(testUser)).thenReturn(Optional.of(existingSubscription));

        Exception exception = assertThrows(IllegalStateException.class,
                () -> subscriptionService.createSubscription(testUser, "price_ABC"));

        assertEquals("El usuario ya tiene una suscripción activa.", exception.getMessage());
    }

    @Test
    void testFindOrCreateStripeCustomer_ExistingCustomer() throws StripeException {
        SubscriptionInfantem existingSubscription = new SubscriptionInfantem();
        existingSubscription.setStripeCustomerId("cus_123456");

        when(subscriptionRepository.findByUser(testUser)).thenReturn(Optional.of(existingSubscription));

        String customerId = subscriptionService.findOrCreateStripeCustomer(testUser);

        assertEquals("cus_123456", customerId);
    }

    @Test
    void testFindOrCreateStripeCustomer_NewCustomer() throws StripeException {
        when(subscriptionRepository.findByUser(testUser)).thenReturn(Optional.empty());

        when(mockCustomer.getId()).thenReturn("cus_654321");
        when(Customer.create(any(CustomerCreateParams.class))).thenReturn(mockCustomer);

        String customerId = subscriptionService.findOrCreateStripeCustomer(testUser);

        assertEquals("cus_654321", customerId);
    }
}
