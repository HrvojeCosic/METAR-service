package com.example.demo.services;


import com.example.demo.domain.entities.Subscription;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.SubscriptionRepository;
import com.example.demo.services.impl.SubscriptionServiceImpl;
import com.example.demo.utils.SubscriptionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class SubscriptionServiceTests {

    private SubscriptionService subscriptionService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    private final Subscription validSubscription = SubscriptionUtils.createValidSubscription();

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);
        subscriptionService = new SubscriptionServiceImpl(subscriptionRepository);

        // Stubbing
        BDDMockito.when(subscriptionRepository.findAll())
                .thenReturn(List.of(validSubscription));
        BDDMockito.when(subscriptionRepository.findByIcaoCode(validSubscription.getIcaoCode()))
                .thenReturn(java.util.Optional.of(validSubscription));
    }

    @Test
    public void testThatGetSubscriptionsReturnsAllSubscriptions() {
        List<Subscription> result = subscriptionService.getSubscriptions();

        Mockito.verify(subscriptionRepository, Mockito.times(1)).findAll();

        assert !result.isEmpty();
        assert result.getFirst().getId().equals(validSubscription.getId());
        assert result.getFirst().getIcaoCode().equals(validSubscription.getIcaoCode());
    }

    @Test
    public void testThatGetSubscriptionReturnsSubscriptionWhenValidIcaoCode() {
        Subscription result = subscriptionService.getActiveSubscription(validSubscription.getIcaoCode());

        Mockito.verify(subscriptionRepository, Mockito.times(1))
                .findByIcaoCode(validSubscription.getIcaoCode());

        assert result.getIcaoCode().equals(validSubscription.getIcaoCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"INVALID", "", " ", "LDZA"})
    public void testThatGetSubscriptionThrowsResourceNotFoundExceptionWhenBadIcaoCode(String icaoCode) {
        BDDMockito.when(subscriptionRepository.findByIcaoCode(icaoCode))
                .thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> subscriptionService.getActiveSubscription(icaoCode));
    }
}
