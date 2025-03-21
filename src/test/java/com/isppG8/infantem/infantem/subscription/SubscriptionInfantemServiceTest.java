package com.isppG8.infantem.infantem.subscription;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.isppG8.infantem.infantem.subscription.SubscriptionInfantemService;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.subscription.SubscriptionInfantemRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionCreateParams;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SubscriptionInfantemServiceTest {

    @InjectMocks
    private SubscriptionInfantemService subscriptionService;

    @Mock
    private SubscriptionInfantemRepository subscriptionRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("test@example.com");

        Stripe.apiKey = "sk_test_51R4hvZRD1fD8EiuBZOtYucDXLsXF0W5WyVjkVufDGwvqzR4RPJAnB5k91aGi5cAyL6cwY7lyS9ln2aFAWcPXbpbu00XterSqNa"; // Usa una clave válida
    }

    @Test
    void testCreateSubscription_Success() throws StripeException {
        String priceId = "price_123";
        String fakeStripeSubscriptionId = "sub_456";
        String fakeCustomerId = "cus_789";

        when(subscriptionRepository.findByUserAndActiveTrue(testUser)).thenReturn(Optional.empty());

        // Mock de Customer
        Customer mockCustomer = mock(Customer.class);
        when(mockCustomer.getId()).thenReturn(fakeCustomerId);

        try (MockedStatic<Customer> mockedCustomer = mockStatic(Customer.class)) {
            mockedCustomer.when(() -> Customer.create(any(CustomerCreateParams.class))).thenReturn(mockCustomer);

            // Mock de Subscription
            Subscription mockSubscription = mock(Subscription.class);
            when(mockSubscription.getId()).thenReturn(fakeStripeSubscriptionId);

            try (MockedStatic<Subscription> mockedSubscription = mockStatic(Subscription.class)) {
                mockedSubscription.when(() -> Subscription.create(any(SubscriptionCreateParams.class))).thenReturn(mockSubscription);

                String subscriptionId = subscriptionService.createSubscription(testUser, priceId);

                assertNotNull(subscriptionId);
                assertEquals(fakeStripeSubscriptionId, subscriptionId);
                verify(subscriptionRepository, times(1)).save(any());
            }
        }
    }



    @Test
    void testCreateSubscription_UserAlreadySubscribed() {
        when(subscriptionRepository.findByUserAndActiveTrue(testUser)).thenReturn(Optional.of(new SubscriptionInfantem()));
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> 
            subscriptionService.createSubscription(testUser, "price_123"));
        
        assertEquals("El usuario ya tiene una suscripción activa.", exception.getMessage());
    }
}
