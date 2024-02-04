package com.example.demo.utils;

import com.example.demo.domain.dto.request.GetSubscriptionRequestDto;
import com.example.demo.domain.dto.request.SubscribeRequestDto;
import com.example.demo.domain.entities.Subscription;

public class SubscriptionUtils {

    static public SubscribeRequestDto createValidSubscribeRequestDto() {
        return SubscribeRequestDto.builder()
                .icaoCode("LDZA")
                .build();
    }

    static public GetSubscriptionRequestDto getEmptySubscriptionRequestDto() {
        return new GetSubscriptionRequestDto();
    }

    static public Subscription createValidSubscription() {
        return new Subscription(1L, "LDZA", true);
    }
}
