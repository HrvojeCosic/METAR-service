package com.example.demo.services.impl;

import com.example.demo.domain.dto.request.AddMetarRequestDto;
import com.example.demo.domain.entities.Metar;
import com.example.demo.domain.entities.Subscription;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.MetarRepository;
import com.example.demo.services.MetarService;
import com.example.demo.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MetarServiceImpl implements MetarService {

    private final MetarRepository metarRepository;
    private final SubscriptionService subscriptionService;

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
        Subscription subscription = subscriptionService.getActiveSubscription(icaoCode);

        return metarRepository.findFirstByIcaoCodeOrderByTimestampDesc(subscription.getIcaoCode())
                .orElseThrow(() -> new ResourceNotFoundException("No METAR found for " + icaoCode));
    }
}
