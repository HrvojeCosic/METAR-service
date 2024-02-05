package com.example.demo.services.impl;

import com.example.demo.domain.dto.request.AddMetarRequestDto;
import com.example.demo.domain.entities.Metar;
import com.example.demo.domain.entities.Subscription;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.MetarRepository;
import com.example.demo.services.MetarFieldStrategy.MetarFieldContext;
import com.example.demo.services.MetarService;
import com.example.demo.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import net.sf.jweather.metar.MetarParseException;
import net.sf.jweather.metar.MetarParser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MetarServiceImpl implements MetarService {

    private final MetarRepository metarRepository;
    private final SubscriptionService subscriptionService;

    @Override
    public Long addMetar(String icaoCode, AddMetarRequestDto addMetarRequestDto) {
        net.sf.jweather.metar.Metar parsed = parseMetar(addMetarRequestDto.getData());

        Metar newMetar = Metar.builder()
                .icaoCode(icaoCode)
                .temperature(parsed.getTemperatureInCelsius())
                .visibility(parsed.getVisibility())
                .windStrength(parsed.getWindSpeedInKnots())
                .timestamp(parsed.getDate())
                .build();

        return metarRepository.save(newMetar).getId();
    }

    @Override
    public Map<String, Object> getMetar(String icaoCode, List<String> projections) {
        Subscription subscription = subscriptionService.getActiveSubscription(icaoCode);

        Metar found = metarRepository.findFirstByIcaoCodeOrderByTimestampDesc(subscription.getIcaoCode())
                .orElseThrow(() -> new ResourceNotFoundException("No METAR found for " + icaoCode));

        MetarFieldContext metarFieldContext = new MetarFieldContext(found);
        return metarFieldContext.createMetarMap(found, projections);
    }

    private net.sf.jweather.metar.Metar parseMetar(String metarData) {
        try {
            net.sf.jweather.metar.Metar parsed = MetarParser.parseReport(metarData);

            if (parsed.getTemperatureInCelsius() == null ||
                    parsed.getVisibility() == null ||
                    parsed.getWindSpeedInKnots() == null ||
                    parsed.getDate() == null) {
                throw new IllegalArgumentException("Invalid METAR data");
            }

            return parsed;
        } catch (MetarParseException e) {
            throw new IllegalArgumentException("Invalid METAR data");
        }
    }
}
