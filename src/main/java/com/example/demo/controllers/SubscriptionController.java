package com.example.demo.controllers;

import com.example.demo.domain.dto.GetSubscriptionsResponseDto;
import com.example.demo.domain.dto.SubscribeRequestDto;
import com.example.demo.domain.dto.UpdateSubscriptionRequestDto;
import com.example.demo.domain.entities.Subscription;
import com.example.demo.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final ModelMapper subscriptionMapper;
    private final SubscriptionService subscriptionService;

    @PostMapping()
    public ResponseEntity<Long> subscribeAirport(@RequestBody SubscribeRequestDto subscriptionRequestDto) {
        Long id = subscriptionService.subscribe(subscriptionMapper.map(subscriptionRequestDto, Subscription.class));

        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<GetSubscriptionsResponseDto> getSubscriptions() {
        return new ResponseEntity<>(subscriptionService.getSubscriptions(), HttpStatus.OK);
    }

    @DeleteMapping("/{icaoCode}")
    public ResponseEntity<Void> unsubscribeAirport(@PathVariable String icaoCode) {
        subscriptionService.unsubscribe(icaoCode);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{icaoCode}")
    public ResponseEntity<Void> updateSubscription(@PathVariable String icaoCode,
                                                   @RequestBody UpdateSubscriptionRequestDto updateSubscriptionRequestDto) {
        Subscription subscription = subscriptionMapper.map(updateSubscriptionRequestDto, Subscription.class);

        subscriptionService.updateSubscription(icaoCode, subscription);
        return ResponseEntity.ok().build();
    }
}
