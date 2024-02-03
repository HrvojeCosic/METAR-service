package com.example.demo.services;

import com.example.demo.domain.dto.AddMetarRequestDto;

public interface MetarService {

    Long addMetar(String icaoCode, AddMetarRequestDto addMetarRequestDto);
}
