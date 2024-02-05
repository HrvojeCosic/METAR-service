package com.example.demo.services.MetarFieldStrategy;

import com.example.demo.domain.entities.Metar;

public class TemperatureStrategy implements MetarFieldStrategy {

    @Override
    public Object extract(Metar metar) {
        return metar.getTemperature();
    }
}