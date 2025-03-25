package com.isppG8.infantem.infantem.subscription;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.isppG8.infantem.infantem.InfantemApplication;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserService;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest(classes = {InfantemApplication.class, SubscriptionInfantemService.class, SubscriptionServiceTest.TestConfig.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Import(SubscriptionServiceTest.TestConfig.class)
public class SubscriptionServiceTest {

    @Autowired
    private SubscriptionInfantemService subscriptionService;

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionInfantemRepository subscriptionRepository;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }

        @Bean
        public SubscriptionInfantemRepository subscriptionInfantemRepository() {
            return mock(SubscriptionInfantemRepository.class);
        }
    }

    @BeforeEach
    public void setup() {
        reset(userService, subscriptionRepository);
    }

    @Test
    public void testActivateSubscription() {
        User user = new User();
        user.setId(1);

        SubscriptionInfantem subscription = new SubscriptionInfantem();
        subscription.setUser(user);

        when(subscriptionRepository.findByUser(user)).thenReturn(Optional.of(subscription));

        subscriptionService.activateSubscription(user, "sub_123");

        assertThat(subscription.isActive()).isTrue();
        assertThat(subscription.getStripeSubscriptionId()).isEqualTo("sub_123");
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    public void testCreateCustomer() throws Exception {
        String email = "test@example.com";
        String name = "Test User";
        String description = "Test description";

        Customer mockCustomer = mock(Customer.class);
        when(mockCustomer.getId()).thenReturn("cus_123");

        CustomerCreateParams params = CustomerCreateParams.builder().setEmail(email).setName(name).setDescription(description).build();
        when(Customer.create(params)).thenReturn(mockCustomer);

        String customerId = subscriptionService.createCustomer(email, name, description);

        assertThat(customerId).isEqualTo("cus_123");
    }
}
