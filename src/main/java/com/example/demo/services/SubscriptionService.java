package com.example.demo.services;

import com.example.demo.domain.entities.Subscription;

import java.util.List;

public interface SubscriptionService {

    Long subscribe(Subscription subscription);

    List<Subscription> getSubscriptions();

    void unsubscribe(String icaoCode);

    Subscription getActiveSubscription(String icaoCode);

    void updateSubscription(String icaoCode, Subscription subscription);
}
