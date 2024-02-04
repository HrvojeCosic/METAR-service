package com.example.demo.services;

import com.example.demo.domain.dto.AddMetarRequestDto;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.MetarRepository;
import com.example.demo.services.impl.MetarServiceImpl;
import com.example.demo.utils.MetarUtils;
import com.example.demo.utils.SubscriptionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class MetarServiceTests {

    private MetarService metarService;

    @Mock
    private MetarRepository metarRepository;

    @Mock
    private SubscriptionService subscriptionService;

    AddMetarRequestDto validAddMetarRequestDto = MetarUtils.createValidAddMetarRequestDto();

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);
        metarService = new MetarServiceImpl(metarRepository, subscriptionService);

        // Stubbing
        BDDMockito.when(subscriptionService.getActiveSubscription(validAddMetarRequestDto.getIcaoCode()))
                .thenReturn(SubscriptionUtils.createValidSubscription());
    }

    @Test
    public void testThatGetMetarThrowsResourceNotFoundExceptionWhenNonExistentMetarInfo() {
        BDDMockito.when(metarRepository.findFirstByIcaoCodeOrderByTimestampDesc(validAddMetarRequestDto.getIcaoCode()))
                .thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> metarService.getMetar(validAddMetarRequestDto.getIcaoCode()));
    }
}
