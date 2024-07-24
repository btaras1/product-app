package com.product.app.mapper;

import com.product.app.model.Product;
import com.product.app.rest.dto.request.ProductRequestDto;
import com.product.app.rest.dto.response.ProductResponseDto;
import com.product.app.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductMapperTest {

    private static final String PRODUCT_CODE = "PRODUCT CODE";
    private static final String PRODUCT_NAME = "PRODUCT NAME";
    private static final BigDecimal PRICE_EUR = BigDecimal.ONE;

    private static final BigDecimal PRICE_USD = BigDecimal.valueOf(1.086);
    private static final boolean available = true;


    @Mock
    CurrencyService currencyService;

    @InjectMocks
    ProductMapper productMapper;
    @Test
    void toEntity() {
        ProductRequestDto requestDto = constructRequestDto();
        given(currencyService.convertEurToUsd(requestDto.getPriceEur())).willReturn(PRICE_USD);

        Product actual = productMapper.toEntity(requestDto);

        assertEquals(requestDto.getName(), actual.getName());
        assertEquals(requestDto.getCode(), actual.getCode());
        assertEquals(requestDto.getPriceEur(), actual.getPriceEur());
        assertEquals(PRICE_USD, actual.getPriceUsd());
        assertTrue(actual.isAvailable());
    }

    @Test
    void toResponse() {
        Product entity = constructEntity();

        ProductResponseDto actual = productMapper.toResponse(entity);

        assertEquals(entity.getCode(), actual.getCode());
        assertEquals(entity.isAvailable(), actual.isAvailable());
        assertEquals(entity.getName(), actual.getName());
        assertEquals(entity.getPriceEur(), actual.getPriceEur());
        assertEquals(entity.getPriceUsd(), actual.getPriceUsd());
    }

    private Product constructEntity() {
        return Product.builder()
                .code(PRODUCT_CODE)
                .isAvailable(available)
                .name(PRODUCT_NAME)
                .priceEur(PRICE_EUR)
                .priceUsd(PRICE_USD)
                .build();
    }

    private ProductRequestDto constructRequestDto() {
        return ProductRequestDto.builder()
                .code(PRODUCT_CODE)
                .available(available)
                .name(PRODUCT_NAME)
                .priceEur(PRICE_EUR)
                .build();
    }
}