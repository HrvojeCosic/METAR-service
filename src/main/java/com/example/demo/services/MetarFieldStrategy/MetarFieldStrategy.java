package com.example.demo.services.MetarFieldStrategy;

import com.example.demo.domain.entities.Metar;

public interface MetarFieldStrategy {

    Object extract(Metar metar);
}