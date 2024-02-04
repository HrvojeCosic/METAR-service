package com.example.demo.utils;

import com.example.demo.domain.dto.request.AddMetarRequestDto;

public class MetarUtils {

    static public AddMetarRequestDto createValidAddMetarRequestDto() {
        return AddMetarRequestDto.builder()
                .data("LDZA 042300Z 24007KT 220V280 CAVOK 06/02 Q1019 NOSIG")
                .build();
    }
}
