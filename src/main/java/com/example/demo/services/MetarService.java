package com.example.demo.services;

import com.example.demo.domain.dto.request.AddMetarRequestDto;
import com.example.demo.domain.entities.Metar;

public interface MetarService {

    Long addMetar(String icaoCode, AddMetarRequestDto addMetarRequestDto);

    Metar getMetar(String icaoCode);

}
