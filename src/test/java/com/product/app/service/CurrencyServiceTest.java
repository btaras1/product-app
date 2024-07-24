package com.product.app.service;

import com.product.app.exceptions.HnbGatewayException;
import com.product.app.gateway.HnbGateway;
import com.product.app.rest.dto.response.HnbApiResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    private final static String USD_CURRENCY = "USD";
    private final static String FIXED_USD_EXCHANGE_RATE = "1.086000";
    private final static String ACTUAL_USD_EXCHANGE_RATE = "1.088000";

    private final static BigDecimal PRICE_EUR = BigDecimal.TEN;

    @Mock
    HnbGateway hnbGateway;

    @InjectMocks
    CurrencyService currencyService;

    @Test
    void convertEurToUsd() {
        given(hnbGateway.getLatestExchangeRate(USD_CURRENCY))
                .willReturn(HnbApiResponseDto.builder()
                        .middleExchangeRate(ACTUAL_USD_EXCHANGE_RATE)
                        .build());

        assertEquals(new BigDecimal(ACTUAL_USD_EXCHANGE_RATE).multiply(PRICE_EUR),
                currencyService.convertEurToUsd(PRICE_EUR));
    }

    @Test
    void convertEurToUsdApiErrorConversionUsingFixedRate() {
        given(hnbGateway.getLatestExchangeRate(USD_CURRENCY))
                .willThrow(new HnbGatewayException(""));

        assertEquals(new BigDecimal(FIXED_USD_EXCHANGE_RATE).multiply(PRICE_EUR),
                currencyService.convertEurToUsd(PRICE_EUR));
    }
}