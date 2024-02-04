package com.example.demo.services;

import com.example.demo.domain.dto.GetSubscriptionsResponseDto;
import com.example.demo.domain.entities.Subscription;

public interface SubscriptionService {

    Long subscribe(Subscription subscription);

    GetSubscriptionsResponseDto getSubscriptions();

    void unsubscribe(String icaoCode);

    Subscription getSubscription(String icaoCode);

    void updateSubscription(String icaoCode, Subscription subscription);
}
