package com.example.demo.repositories;

import com.example.demo.domain.entities.Metar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetarRepository extends JpaRepository<Metar, Long> {

    Metar findFirstByIcaoCodeOrderByTimestampDesc(String icaoCode);
}
