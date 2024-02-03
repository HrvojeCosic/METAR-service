package com.example.demo.utils;

import com.example.demo.domain.dto.AddMetarRequestDto;

public class MetarUtils {

    static public AddMetarRequestDto createValidAddMetarRequestDto() {
        return AddMetarRequestDto.builder()
                .data("031800Z 23003KT CAVOK 06/01 Q1023 NOSIG")
                .icaoCode("LDZA")
                .timestamp("2024/02/03 18:00")
                .build();
    }
}
