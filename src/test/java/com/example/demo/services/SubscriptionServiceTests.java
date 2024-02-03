package com.example.demo.services;


import com.example.demo.domain.dto.GetSubscriptionsResponseDto;
import com.example.demo.domain.entities.Subscription;
import com.example.demo.repositories.SubscriptionRepository;
import com.example.demo.services.impl.SubscriptionServiceImpl;
import com.example.demo.utils.SubscriptionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class SubscriptionServiceTests {

    private SubscriptionServiceImpl subscriptionService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    private final Subscription validSubscription = SubscriptionUtils.createValidSubscription();

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);
        subscriptionService = new SubscriptionServiceImpl(subscriptionRepository);

        // Stubbing
        BDDMockito.when(subscriptionRepository.findAllByActive(true))
                .thenReturn(List.of(validSubscription));
    }

    @Test
    public void testThatGetSubscriptionsReturnsSubscriptions() {
        GetSubscriptionsResponseDto result = subscriptionService.getSubscriptions();

        Mockito.verify(subscriptionRepository, Mockito.times(1))
                .findAllByActive(true);

        assert !result.getSubscriptions().isEmpty();
        assert result.getSubscriptions().get(0).getIcaoCode().equals(validSubscription.getIcaoCode());
    }
}
