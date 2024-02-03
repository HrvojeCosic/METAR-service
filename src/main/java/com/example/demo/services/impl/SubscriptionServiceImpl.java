package com.example.demo.services.impl;

import com.example.demo.domain.dto.GetSubscriptionsResponseDto;
import com.example.demo.domain.dto.SubscriptionDto;
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
        } else {
            subscription.setActive(true);
            return subscriptionRepository.save(subscription).getId();
        }
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

    @Override
    public void unsubscribe(String icaoCode) {
        Optional<Subscription> existingSubscription = subscriptionRepository.findByIcaoCode(icaoCode);

        if (existingSubscription.isEmpty()) {
            throw new ResourceNotFoundException("Subscription with given ICAO code does not exist");
        }

        Subscription subscription = existingSubscription.get();
        subscription.setActive(false);
        subscriptionRepository.save(subscription);
    }
}
