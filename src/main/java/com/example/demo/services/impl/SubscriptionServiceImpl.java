package com.example.demo.services.impl;

import com.example.demo.domain.dto.GetSubscriptionsResponseDto;
import com.example.demo.domain.dto.SubscriptionDto;
import com.example.demo.domain.entities.Subscription;
import com.example.demo.repositories.SubscriptionRepository;
import com.example.demo.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Long subscribe(Subscription subscription) {
        subscription.setActive(true);
        return subscriptionRepository.save(subscription).getId();
    }

    @Override
    public GetSubscriptionsResponseDto getSubscriptions() {
        List<SubscriptionDto> subscriptions = subscriptionRepository
                .findAllByActive(true).stream().map(subscription ->
                        SubscriptionDto.builder()
                                .icaoCode(subscription.getIcaoCode())
                                .build()
                ).toList();

        return new GetSubscriptionsResponseDto(subscriptions);
    }
}
