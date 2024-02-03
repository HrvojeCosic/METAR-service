package com.example.demo.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "metar")
public class Metar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String icaoCode;

    @Setter
    @Column(nullable = false)
    private String data;

    @Setter
    @Column(nullable = false)
    private Timestamp timestamp;
}
