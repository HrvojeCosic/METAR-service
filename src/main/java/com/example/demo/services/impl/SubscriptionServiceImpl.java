package com.example.demo.services.impl;

import com.example.demo.domain.entities.Subscription;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.SubscriptionRepository;
import com.example.demo.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Long subscribe(Subscription subscription) {
        Optional<Subscription> existingSubscription = subscriptionRepository.findByIcaoCode(subscription.getIcaoCode());

        if (existingSubscription.isPresent()) {
            Subscription foundSub = existingSubscription.get();

            if (!foundSub.isActive()) {
                foundSub.setActive(true);
                subscriptionRepository.save(foundSub);
            }

            return foundSub.getId();
        }

        subscription.setActive(true);
        return subscriptionRepository.save(subscription).getId();
    }

    @Override
    public List<Subscription> getSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Subscription getActiveSubscription(String icaoCode) {
        String errorMessage = "Subscription with given ICAO code does not exist";

        Subscription subscription = subscriptionRepository.findByIcaoCode(icaoCode)
                .orElseThrow(() -> new ResourceNotFoundException(errorMessage));

        if (!subscription.isActive()) {
            throw new ResourceNotFoundException(errorMessage);
        }

        return subscription;
    }

    @Override
    public void unsubscribe(String icaoCode) {
        Subscription subscription = subscriptionRepository.findByIcaoCode(icaoCode)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription with given ICAO code does not exist"));

        subscription.setActive(false);
        subscriptionRepository.save(subscription);
    }

    @Override
    public void updateSubscription(String icaoCode, Subscription updatedSubscription) {
        Subscription existingSubscription = subscriptionRepository.findByIcaoCode(icaoCode)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription with given ICAO code does not exist"));

        existingSubscription.setActive(updatedSubscription.isActive());
        subscriptionRepository.save(existingSubscription);
    }
}
