package com.example.demo.services;

import com.example.demo.domain.dto.request.AddMetarRequestDto;
import com.example.demo.domain.entities.Metar;
import com.example.demo.domain.entities.Subscription;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.MetarRepository;
import com.example.demo.services.impl.MetarServiceImpl;
import com.example.demo.utils.MetarUtils;
import com.example.demo.utils.SubscriptionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
    Subscription validSubscription = SubscriptionUtils.createValidSubscription();


    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);
        metarService = new MetarServiceImpl(metarRepository, subscriptionService);

        // Stubbing
        BDDMockito.when(subscriptionService.getActiveSubscription(validSubscription.getIcaoCode()))
                .thenReturn(SubscriptionUtils.createValidSubscription());
    }

    @Test
    public void testThatGetMetarThrowsResourceNotFoundExceptionWhenNonExistentMetarInfo() {
        BDDMockito.when(metarRepository.findFirstByIcaoCodeOrderByTimestampDesc(validSubscription.getIcaoCode()))
                .thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> metarService.getMetar(validSubscription.getIcaoCode()));
    }

    @Test
    public void testThatParseMetarReturnsMetarObjectWhenValidMetarFormat() {
        String validMetarString = "2024/02/04 13:30 LDZA 041330Z 29003KT 220V350 CAVOK 14/03 Q1021 NOSIG";
        Metar metar = metarService.parseMetar(validMetarString);
        assert metar != null;
        assert metar.getIcaoCode().equals("LDZA");
        assert metar.getTimestamp().toString().equals("2024-02-04T13:30");
        assert metar.getWindStrength().equals("29003KT");
        assert metar.getVisibility().equals("CAVOK");
        assert metar.getTemperature().equals("14/03");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2024/02/04 13:30 LDZA 041330Z 29003KT 220V350 CAVOK Q1021 NOSIG", // missing temperature
            "LDZA 041130Z VRB02dddKT CAVOK 11/04 Q1023 NOSIG", // missing time
            "2021/08/01 12:00 METAR",
            "2021/08/01 12:00",
            "2021/08/01"
    })
    public void testThatParseMetarThrowsIllegalArgumentExceptionWhenInvalidMetarFormat(String invalidMetarString) {
        assertThrows(IllegalArgumentException.class, () -> metarService.parseMetar(invalidMetarString));
    }
}
