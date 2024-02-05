package com.example.demo.services.MetarFieldStrategy;

import com.example.demo.domain.entities.Metar;

public class TimestampStrategy extends MetarFieldStrategy {

    public TimestampStrategy(Metar metar) {
        super(metar);
    }

    @Override
    String toNaturalLanguage() {
        return String.format("The timestamp of this METAR data is: %s. ", metar.getTimestamp());
    }

    @Override
    public Object extract(Metar metar) {
        return metar.getTimestamp();
    }
}