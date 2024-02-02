package com.example.demo.utils;

import com.example.demo.domain.dto.SubscribeRequestDto;

public class SubscriptionUtils {

    static public SubscribeRequestDto createValidSubscribeRequestDto() {
        return SubscribeRequestDto.builder()
                .icaoCode("LDZA")
                .build();
    }
}
