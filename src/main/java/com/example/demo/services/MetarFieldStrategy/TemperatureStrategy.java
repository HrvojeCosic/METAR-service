package com.example.demo.services.MetarFieldStrategy;

import com.example.demo.domain.entities.Metar;

public class TemperatureStrategy extends MetarFieldStrategy {

    public TemperatureStrategy(Metar metar) {
        super(metar);
    }

    @Override
    public Object extract(Metar metar) {
        return metar.getTemperature();
    }

    @Override
    public String toNaturalLanguage() {
        return String.format("The temperature is %s degrees Celsius. ", metar.getTemperature());
    }
}