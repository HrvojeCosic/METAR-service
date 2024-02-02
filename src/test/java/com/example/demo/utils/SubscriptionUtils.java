package com.example.demo.utils;

import com.example.demo.domain.dto.SubscribeRequestDto;
import com.example.demo.domain.entities.Subscription;

public class SubscriptionUtils {

    static public SubscribeRequestDto createValidSubscribeRequestDto() {
        return SubscribeRequestDto.builder()
                .icaoCode("LDZA")
                .build();
    }

    static public Subscription createValidSubscription() {
        return new Subscription(1L, "LDZA", true);
    }
}
