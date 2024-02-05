package com.example.demo.services.MetarFieldStrategy;

import com.example.demo.domain.entities.Metar;

public class VisibilityStrategy implements MetarFieldStrategy {

    @Override
    public Object extract(Metar metar) {
        return metar.getVisibility();
    }
}