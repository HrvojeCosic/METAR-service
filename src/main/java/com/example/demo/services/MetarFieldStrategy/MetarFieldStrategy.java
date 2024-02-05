package com.example.demo.services.MetarFieldStrategy;

import com.example.demo.domain.entities.Metar;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class MetarFieldStrategy {

    protected final Metar metar;

    abstract String toNaturalLanguage();
    abstract Object extract(Metar metar);
}