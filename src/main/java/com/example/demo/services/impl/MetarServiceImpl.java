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
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MetarServiceImpl implements MetarService {

    private final MetarRepository metarRepository;
    private final SubscriptionService subscriptionService;

    @Override
    public Long addMetar(String icaoCode, AddMetarRequestDto addMetarRequestDto) {
        Metar newMetar = parseMetar(addMetarRequestDto.getData());
        return metarRepository.save(newMetar).getId();
    }

    @Override
    public Metar getMetar(String icaoCode) {
        Subscription subscription = subscriptionService.getActiveSubscription(icaoCode);

        return metarRepository.findFirstByIcaoCodeOrderByTimestampDesc(subscription.getIcaoCode())
                .orElseThrow(() -> new ResourceNotFoundException("No METAR found for " + icaoCode));
    }

    @Override
    public Metar parseMetar(String metarString) {
        String regex = "(\\S+)\\s(\\S+)\\s(\\S+)\\s(\\S+)\\s(\\S+)\\s(\\S+)\\s(\\S+)\\s(\\S+)\\s(\\S+)\\s(\\S+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(metarString);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid METAR format");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return Metar.builder()
                .timestamp(LocalDateTime.parse(matcher.group(1) + " " + matcher.group(2), formatter))
                .icaoCode(matcher.group(3))
                .windStrength(matcher.group(5))
                .visibility(matcher.group(7))
                .temperature(matcher.group(8))
                .build();
    }
}
