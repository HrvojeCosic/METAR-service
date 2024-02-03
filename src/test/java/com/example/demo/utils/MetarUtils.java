package com.example.demo.utils;

import com.example.demo.domain.dto.AddMetarRequestDto;

public class MetarUtils {

    static public AddMetarRequestDto createValidAddMetarRequestDto() {
        return AddMetarRequestDto.builder()
                .data("METAR LDZA 121200Z 0902MPS 090V150 2000 R04/P2000N R22/P2000N OVC050 0/M01 Q1020=")
                .build();
    }
}
