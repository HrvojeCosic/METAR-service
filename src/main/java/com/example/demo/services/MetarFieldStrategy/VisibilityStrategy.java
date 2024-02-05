package com.example.demo.services.MetarFieldStrategy;

import com.example.demo.domain.entities.Metar;

public class VisibilityStrategy extends MetarFieldStrategy {

    public VisibilityStrategy(Metar metar) {
        super(metar);
    }

    @Override
    String toNaturalLanguage() {
        return String.format("The visibility is %s meters. ", metar.getVisibility());
    }

    @Override
    public Object extract(Metar metar) {
        return metar.getVisibility();
    }
}