package com.example.demo.services;

import com.example.demo.domain.dto.request.AddMetarRequestDto;

import java.util.List;
import java.util.Map;

public interface MetarService {

    Long addMetar(String icaoCode, AddMetarRequestDto addMetarRequestDto);

    Map<String, Object> getMetar(String icaoCode, List<String> projectionFields);

}
