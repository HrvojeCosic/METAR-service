package com.example.demo.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMetarResponseDto {

    private String icaoCode;
    private Date timestamp;
    private Float windStrength;
    private Float temperature;
    private Float visibility;
}
