package com.example.demo.services.impl;

import com.example.demo.domain.dto.request.GetSubscriptionRequestDto;
import com.example.demo.domain.entities.Subscription;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.SubscriptionRepository;
import com.example.demo.services.SubscriptionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final EntityManager entityManager;

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
    public List<Subscription> getSubscriptions(GetSubscriptionRequestDto getSubscriptionRequestDto) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Subscription> criteriaQuery = criteriaBuilder.createQuery(Subscription.class);
        Root<Subscription> root = criteriaQuery.from(Subscription.class);

        String icaoCode = getSubscriptionRequestDto.getIcaoCode();
        Boolean active = getSubscriptionRequestDto.getActive();

        List<Predicate> searchCriteria = new ArrayList<>();

        /*
         * Search criteria for query
         */
        if (icaoCode != null) {
            searchCriteria.add(criteriaBuilder.like(root.get("icaoCode"), "%" + icaoCode + "%"));        }
        if (active != null) {
            searchCriteria.add(criteriaBuilder.equal(root.get("active"), active));
        }

        criteriaQuery.select(root).where(criteriaBuilder.and(searchCriteria.toArray(new Predicate[searchCriteria.size()])));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Subscription getActiveSubscription(String icaoCode) {
        Subscription subscription = findSubscription(icaoCode);

        if (!subscription.isActive()) {
            throw new ResourceNotFoundException("Subscription with given ICAO code is not active");
        }

        return subscription;
    }

    @Override
    public void unsubscribe(String icaoCode) {
        Subscription subscription = findSubscription(icaoCode);

        subscription.setActive(false);
        subscriptionRepository.save(subscription);
    }

    @Override
    public void updateSubscription(String icaoCode, Subscription updatedSubscription) {
        Subscription subscription = findSubscription(icaoCode);

        subscription.setActive(updatedSubscription.isActive());
        subscriptionRepository.save(subscription);
    }

    private Subscription findSubscription(String icaoCode) {
        return subscriptionRepository.findByIcaoCode(icaoCode)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription with given ICAO code does not exist"));
    }
}
