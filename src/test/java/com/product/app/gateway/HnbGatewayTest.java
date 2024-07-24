package com.product.app.gateway;

import com.product.app.exceptions.HnbGatewayException;
import com.product.app.rest.dto.response.HnbApiResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class HnbGatewayTest {

    private static final String HNB_API_EXCEPTION_MESSAGE = "HNB Api is not responding at the time, will update the price on next get request.";

    @Mock
    RestTemplate restTemplate;

    HnbGateway hnbGateway = new HnbGateway();

    @Test
    void getLatestExchangeRate() {
        ReflectionTestUtils.setField(hnbGateway, "restTemplate", restTemplate);


        ResponseEntity<List<HnbApiResponseDto>> variable = new ResponseEntity<>(List.of(constructHnbApiResponse()), HttpStatus.OK);

        when(restTemplate.exchange(ArgumentMatchers.any(URI.class),
                ArgumentMatchers.eq(HttpMethod.GET),
                ArgumentMatchers.any(),
                ArgumentMatchers.eq(new ParameterizedTypeReference<List<HnbApiResponseDto>>() {
                }))).thenReturn(variable);

        HnbApiResponseDto actual = hnbGateway.getLatestExchangeRate("USD");

        assertEquals(constructHnbApiResponse().getMiddleExchangeRate(), actual.getMiddleExchangeRate());
    }

    @Test
    void getLatestExchangeRate_apiResponseError_shouldThrowException() {

        ReflectionTestUtils.setField(hnbGateway, "restTemplate", restTemplate);

        ResponseEntity<List<HnbApiResponseDto>> variable = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.exchange(ArgumentMatchers.any(URI.class),
                ArgumentMatchers.eq(HttpMethod.GET),
                ArgumentMatchers.any(),
                ArgumentMatchers.eq(new ParameterizedTypeReference<List<HnbApiResponseDto>>() {
                }))).thenReturn(variable);

        Exception exception = assertThrows(
                HnbGatewayException.class,
                () -> hnbGateway.getLatestExchangeRate("USD"));


        assertEquals(HNB_API_EXCEPTION_MESSAGE, exception.getMessage());
    }

    private HnbApiResponseDto constructHnbApiResponse() {
        return HnbApiResponseDto.builder()
                .id(1L)
                .middleExchangeRate("1,0888")
                .build();
    }
}
