package com.example.demo.services.MetarFieldStrategy;

import com.example.demo.domain.entities.Metar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetarFieldContext {
    private final Map<String, MetarFieldStrategy> strategyMap;

    public MetarFieldContext() {
        this.strategyMap = new HashMap<>();
        strategyMap.put("temperature", new TemperatureStrategy());
        strategyMap.put("visibility", new VisibilityStrategy());
        strategyMap.put("windStrength", new WindStrengthStrategy());
        strategyMap.put("timestamp", new TimestampStrategy());
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
        }

        return metarMap;
    }
}