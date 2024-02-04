package com.example.demo.integration;

import com.example.demo.domain.dto.request.GetSubscriptionRequestDto;
import com.example.demo.domain.dto.request.SubscribeRequestDto;
import com.example.demo.domain.dto.request.UpdateSubscriptionRequestDto;
import com.example.demo.utils.SubscriptionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class SubscriptionControllerIntegrationTests {

    private final MockMvc mockMvc;

    SubscribeRequestDto postSubReq = SubscriptionUtils.createValidSubscribeRequestDto();
    GetSubscriptionRequestDto getSubReq = SubscriptionUtils.getEmptySubscriptionRequestDto();

    @Autowired
    public SubscriptionControllerIntegrationTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void testThatSubscribeAirportReturns201WhenValidRequest() throws Exception {
        mockMvc.perform(post("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(postSubReq)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testThatSubscribeAirportDoesntCreateDuplicateSubscriptions() throws Exception {
        // Subscribe airport
        mockMvc.perform(post("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(postSubReq)))
                .andExpect(status().isCreated());

        // Subscribe airport again
        mockMvc.perform(post("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(postSubReq)))
                .andExpect(status().isCreated());

        // Check if only one subscription is created
        mockMvc.perform(get("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(getSubReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].icaoCode").value(postSubReq.getIcaoCode()))
                .andExpect(jsonPath("$[1].icaoCode").doesNotExist());
    }

    @Test
    public void testThatGetSubscriptionsReturns200AndSubscriptionsWhenValidRequest() throws Exception {
        // Subscribe airport
        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postSubReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber());

        // Get subscriptions
        mockMvc.perform(get("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(getSubReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].icaoCode").value(postSubReq.getIcaoCode()));
    }

    @Test
    public void testThatGetSubscriptionsReturnsFilteredSubscriptionsWhenFilterProvided() throws Exception {

        // Subscribe airport
        mockMvc.perform(post("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(postSubReq)))
                .andExpect(status().isCreated());

        // Get subscriptions
        getSubReq.setIcaoCode(postSubReq.getIcaoCode().substring(0, 2));
        getSubReq.setActive(true);
        mockMvc.perform(get("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(getSubReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].icaoCode").value(postSubReq.getIcaoCode()));

        getSubReq.setActive(false);
        mockMvc.perform(get("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(getSubReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testThatUnsubscribeAirportDeactivatesSubscriptionAndReturns200WhenValidRequest() throws Exception {
        // Subscribe airport
        mockMvc.perform(post("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(postSubReq)))
                .andExpect(status().isCreated());

        // Unsubscribe airport
        mockMvc.perform(delete(String.format("/subscriptions/%s", postSubReq.getIcaoCode()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Check if subscription is deactivated
        mockMvc.perform(get("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(getSubReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].active").value(false));
    }

    @Test
    public void testThatUnsubscribeAirportReturns404WhenAirportNonExistent() throws Exception {
        mockMvc.perform(delete("/subscriptions/INVALID_ICAO_CODE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testThatUpdateSubscriptionReturns200WhenValidRequest() throws Exception {
        UpdateSubscriptionRequestDto updateSubDto = UpdateSubscriptionRequestDto
                .builder()
                .active(false)
                .build();

        // Subscribe airport
        mockMvc.perform(post("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(postSubReq)))
                .andExpect(status().isCreated());

        // Update subscription (deactivate it)
        mockMvc.perform(put(String.format("/subscriptions/%s", postSubReq.getIcaoCode()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateSubDto)))
                .andExpect(status().isOk());

        // Check if subscription is deactivated
        mockMvc.perform(get("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(getSubReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].active").value(false));
    }

    @ParameterizedTest
    @ValueSource(strings = {"INVALID", "", " ", "LDZA"})
    public void testThatUpdateSubscriptionReturns404WhenBadIcaoCodeProvided(String icaoCode) throws Exception {
        UpdateSubscriptionRequestDto updateSubDto = UpdateSubscriptionRequestDto
                .builder()
                .active(false)
                .build();

        mockMvc.perform(put("/subscriptions/" + icaoCode)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateSubDto)))
                .andExpect(status().isNotFound());
    }
}
