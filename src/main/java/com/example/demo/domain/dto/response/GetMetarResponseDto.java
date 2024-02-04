package com.example.demo.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMetarResponseDto {

    private String icaoCode;
    private String timestamp;
    private String windStrength;
    private String temperature;
    private String visibility;
}
