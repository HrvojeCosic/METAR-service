package com.example.demo.services.MetarFieldStrategy;

import com.example.demo.domain.entities.Metar;

public class WindStrengthStrategy extends MetarFieldStrategy {

    public WindStrengthStrategy(Metar metar) {
        super(metar);
    }

    @Override
    String toNaturalLanguage() {
        return String.format("The wind strength is %s knots. ", metar.getWindStrength());
    }

    @Override
    public Object extract(Metar metar) {
        return metar.getWindStrength();
    }
}