package com.example.demo.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddMetarRequestDto {

    private String data;
    private String icaoCode;
    private LocalDateTime timestamp;
}
