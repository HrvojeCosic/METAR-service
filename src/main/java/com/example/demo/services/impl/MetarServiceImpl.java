package com.example.demo.services.impl;

import com.example.demo.domain.dto.AddMetarRequestDto;
import com.example.demo.domain.entities.Metar;
import com.example.demo.repositories.MetarRepository;
import com.example.demo.services.MetarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MetarServiceImpl implements MetarService {

    private final MetarRepository metarRepository;

    @Override
    public Long addMetar(String icaoCode, AddMetarRequestDto addMetarRequestDto) {
        Metar newMetar = Metar.builder()
                .icaoCode(icaoCode)
                .data(addMetarRequestDto.getData())
                .timestamp(LocalDateTime.now())
                .build();

        return metarRepository.save(newMetar).getId();
    }

    @Override
    public Metar getMetar(String icaoCode) {
        return metarRepository.findFirstByIcaoCodeOrderByTimestampDesc(icaoCode);
    }
}
