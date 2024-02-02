package com.example.demo.services;


import com.example.demo.domain.dto.GetSubscriptionsResponseDto;
import com.example.demo.domain.entities.Subscription;
import com.example.demo.repositories.SubscriptionRepository;
import com.example.demo.utils.SubscriptionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class SubscriptionServiceTests {

    private final MockMvc mockMvc;

    @InjectMocks
    private final SubscriptionService subscriptionService;

    @Mock
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionServiceTests(MockMvc mockMvc, SubscriptionService subscriptionService, SubscriptionRepository subscriptionRepository) {
        this.mockMvc = mockMvc;
        this.subscriptionService = subscriptionService;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Test
    public void testThatGetSubscriptionsReturnsSubscriptions() {
        Subscription validSubscription = SubscriptionUtils.createValidSubscription();

        BDDMockito.when(subscriptionRepository.findAllByActive(true))
                .thenReturn(List.of(validSubscription));

        GetSubscriptionsResponseDto result = subscriptionService.getSubscriptions();
        assert result.getSubscriptions().size() == 1;
        assert result.getSubscriptions().get(0).getIcaoCode().equals(validSubscription.getIcaoCode());
    }
}
