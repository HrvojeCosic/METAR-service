package com.example.demo.repositories;

import com.example.demo.domain.entities.Metar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetarRepository extends JpaRepository<Metar, Long> {

    Optional<Metar> findFirstByIcaoCodeOrderByTimestampDesc(String icaoCode);
}
