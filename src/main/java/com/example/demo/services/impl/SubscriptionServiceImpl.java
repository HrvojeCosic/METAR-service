package com.example.demo.services.impl;

import com.example.demo.domain.entities.Subscription;
import com.example.demo.services.SubscriptionService;
import com.example.demo.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Long subscribe(Subscription subscription) {
        return subscriptionRepository.save(subscription).getId();
    }
}
