package com.isppG8.infantem.infantem.subscription;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(SubscriptionInfantemController.class)
@WithMockUser(username = "testUser", roles = { "USER" })
public class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private SubscriptionInfantemService subscriptionService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public SubscriptionInfantemService intakeService() {
            return Mockito.mock(SubscriptionInfantemService.class);
        }
    }

    @Test
    public void testCreateSubscription() throws Exception {
        mockMvc.perform(post("/api/v1/subscriptions/create").param("userId", "1").param("customerId", "cus_123")
                .param("priceId", "price_abc").param("paymentMethodId", "pm_456")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testCancelSubscription() throws Exception {
        doNothing().when(subscriptionService).cancelSubscription("sub_789");

        mockMvc.perform(post("/api/v1/subscriptions/cancel").param("subscriptionId", "sub_789")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().string("Suscripción cancelada exitosamente."));
    }

    @Test
    public void testGetCustomersByEmail() throws Exception {
        Map<String, Object> customer = new HashMap<>();
        customer.put("id", "cus_123");
        when(subscriptionService.getCustomersByEmail("test@example.com")).thenReturn(List.of(customer));

        mockMvc.perform(
                get("/api/v1/subscriptions/customers").param("email", "test@example.com").param("lasts4", "1234"))
                .andExpect(status().isOk());
    }
}
