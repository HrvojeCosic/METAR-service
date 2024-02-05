package com.example.demo.services.MetarFieldStrategy;

import com.example.demo.domain.entities.Metar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetarFieldContext {
    private final Map<String, MetarFieldStrategy> strategyMap;

    public MetarFieldContext(Metar metar) {
        this.strategyMap = new HashMap<>();
        strategyMap.put("temperature", new TemperatureStrategy(metar));
        strategyMap.put("visibility", new VisibilityStrategy(metar));
        strategyMap.put("windStrength", new WindStrengthStrategy(metar));
        strategyMap.put("timestamp", new TimestampStrategy(metar));
    }

    public Map<String, Object> createMetarMap(Metar found, List<String> projections) {
        Map<String, Object> metarMap = new HashMap<>();

        if (projections.isEmpty()) {
            projections = strategyMap.keySet().stream().toList();
        }

        for (String field : projections) {
            MetarFieldStrategy strategy = strategyMap.get(field);
            if (strategy == null) {
                throw new IllegalArgumentException("Invalid projection field: " + field);
            }
            metarMap.put(field, strategy.extract(found));
            metarMap.put("explanation", metarMap.getOrDefault("explanation", "") + strategy.toNaturalLanguage());
        }

        return metarMap;
    }
}