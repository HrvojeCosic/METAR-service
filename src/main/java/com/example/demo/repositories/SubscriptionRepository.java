package com.example.demo.repositories;

import com.example.demo.domain.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByIcaoCode(String icaoCode);
}
