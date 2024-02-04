package com.example.demo.utils;

import com.example.demo.domain.dto.request.AddMetarRequestDto;

public class MetarUtils {

    static public AddMetarRequestDto createValidAddMetarRequestDto() {
        return AddMetarRequestDto.builder()
                .data("2024/02/04 12:00 LDZA 041200Z 29003KT 230V350 CAVOK 11/04 Q1022 NOSIG")
                .build();
    }
}
