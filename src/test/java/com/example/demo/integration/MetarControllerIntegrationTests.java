package com.example.demo.integration;

import com.example.demo.domain.dto.AddMetarRequestDto;
import com.example.demo.utils.MetarUtils;
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

        addMetarRequestDto2.setData("METAR LDZA 031630Z 04003KT CAVOK 08/02 Q1023 NOSIG");
        String icaoCode = "LDZA";

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
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").value(addMetarRequestDto2.getData()));
    }
}
