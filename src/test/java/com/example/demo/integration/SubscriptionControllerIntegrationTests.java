package com.example.demo.integration;

import com.example.demo.domain.dto.SubscribeRequestDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class SubscriptionControllerIntegrationTests {

    private final MockMvc mockMvc;

    @Autowired
    public SubscriptionControllerIntegrationTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void testThatSubscribeAirportReturns201WhenValidRequest() throws Exception {
        SubscribeRequestDto subDto = SubscriptionUtils.createValidSubscribeRequestDto();

        mockMvc.perform(post("/airport/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(subDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testThatUnsubscribeAirportDeactivatesSubscriptionAndReturns200WhenValidRequest() throws Exception {
        SubscribeRequestDto subDto = SubscriptionUtils.createValidSubscribeRequestDto();

        // Subscribe airport
        mockMvc.perform(post("/airport/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(subDto)))
                .andExpect(status().isCreated());

        // Unsubscribe airport
        mockMvc.perform(delete(String.format("/airport/subscriptions/%s", subDto.getIcaoCode()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Check if subscription is deactivated
        mockMvc.perform(get("/airport/subscriptions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].icaoCode").doesNotExist());
    }

    @Test
    public void testThatUnsubscribeAirportReturns404WhenAirportNonExistent() throws Exception {
        mockMvc.perform(delete("/airport/subscriptions/INVALID_ICAO_CODE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
