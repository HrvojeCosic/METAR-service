package com.example.demo.integration;

import com.example.demo.domain.dto.request.AddMetarRequestDto;
import com.example.demo.domain.dto.request.SubscribeRequestDto;
import com.example.demo.utils.MetarUtils;
import com.example.demo.utils.SubscriptionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class MetarControllerIntegrationTests {

    private final MockMvc mockMvc;

    @Autowired
    public MetarControllerIntegrationTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void testThatAddMetarReturns201WhenValidRequest() throws Exception {
        AddMetarRequestDto addMetarRequestDto = MetarUtils.createValidAddMetarRequestDto();
        String icaoCode = "LDZA";

        mockMvc.perform(post(String.format("/airport/%s/METAR", icaoCode))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addMetarRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testThatGetMetarReturnsLatestMetarWhenValidRequest() throws Exception {
        AddMetarRequestDto addMetarRequestDto1 = MetarUtils.createValidAddMetarRequestDto();
        AddMetarRequestDto addMetarRequestDto2 = MetarUtils.createValidAddMetarRequestDto();
        SubscribeRequestDto subDto = SubscriptionUtils.createValidSubscribeRequestDto();

        addMetarRequestDto2.setData("LDZA 041330Z 29003KT 220V350 CAVOK 14/03 Q1021 NOSIG");
        String icaoCode = subDto.getIcaoCode();

        // Subscribe
        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(subDto)))
                .andExpect(status().isCreated());

        // Add first METAR
        mockMvc.perform(post(String.format("/airport/%s/METAR", icaoCode))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addMetarRequestDto1)))
                .andExpect(status().isCreated());

        // Add second METAR
        mockMvc.perform(post(String.format("/airport/%s/METAR", icaoCode))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addMetarRequestDto2)))
                .andExpect(status().isCreated());

        // Get latest METAR
        assert !Objects.equals(addMetarRequestDto1.getData(), addMetarRequestDto2.getData());
        mockMvc.perform(get(String.format("/airport/%s/METAR", icaoCode))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.windStrength").exists())
                .andExpect(jsonPath("$.windStrength").isNotEmpty())
                .andExpect(jsonPath("$.temperature").exists())
                .andExpect(jsonPath("$.temperature").isNotEmpty())
                .andExpect(jsonPath("$.visibility").exists())
                .andExpect(jsonPath("$.visibility").isNotEmpty());
    }

    @Test
    public void testThatGetMetarReturnsProjectedMetarWhenValidRequest() throws Exception {
        AddMetarRequestDto addMetarRequestDto = MetarUtils.createValidAddMetarRequestDto();
        SubscribeRequestDto subDto = SubscriptionUtils.createValidSubscribeRequestDto();

        String icaoCode = subDto.getIcaoCode();

        // Subscribe
        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(subDto)))
                .andExpect(status().isCreated());

        // Add METAR
        mockMvc.perform(post(String.format("/airport/%s/METAR", icaoCode))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addMetarRequestDto)))
                .andExpect(status().isCreated());

        // Get projected METAR
        mockMvc.perform(get(String.format("/airport/%s/METAR?projections=timestamp,windStrength", icaoCode))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.windStrength").exists())
                .andExpect(jsonPath("$.windStrength").isNotEmpty())
                .andExpect(jsonPath("$.temperature").doesNotExist())
                .andExpect(jsonPath("$.visibility").doesNotExist());
    }

    @Test
    public void testThatAddMetarReturns400WhenInvalidMetarDataProvided() throws Exception {
        AddMetarRequestDto addMetarRequestDto = MetarUtils.createValidAddMetarRequestDto();
        addMetarRequestDto.setData("Invalid METAR data");
        String icaoCode = "LDZA";

        mockMvc.perform(post(String.format("/airport/%s/METAR", icaoCode))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addMetarRequestDto)))
                .andExpect(status().isBadRequest());
    }
}
