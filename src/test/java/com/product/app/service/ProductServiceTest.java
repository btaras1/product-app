package com.product.app.service;

import com.product.app.model.Product;
import com.product.app.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private static final String SORT_FIELD = "FIELD";
    private static final String SORT_DIRECTION = "ASC";
    private static int PAGE_NUM = 2;
    private static int PAGE_SIZE = 2;
    private static final String PRODUCT_CODE = "PRODUCT CODE";
    private static final String PRODUCT_NAME = "PRODUCT NAME";
    private static final BigDecimal PRICE_EUR = BigDecimal.ONE;
    private static final UUID PRODUCT_ID = UUID.randomUUID();

    private final static BigDecimal FIXED_USD_EXCHANGE_RATE = new BigDecimal("1.086000");

    private static final BigDecimal PRICE_USD = BigDecimal.valueOf(1.087);
    private static final boolean available = true;

    @Mock
    ProductRepository repository;

    @Mock
    CurrencyService currencyService;

    @InjectMocks
    ProductService service;

    @Captor
    ArgumentCaptor<Product> productCaptor;

    @Test
    void create() {
        Product entity = constructEntity();

        given(repository.save(entity)).willReturn(entity);

        Product actual = service.create(entity);

        assertEquals(entity, actual);
    }

    @Test
    void findById() {
        given(repository.findById(PRODUCT_ID)).willReturn(Optional.of(constructEntity()));
        given(currencyService.convertEurToUsd(PRICE_EUR)).willReturn(FIXED_USD_EXCHANGE_RATE);

        service.findById(PRODUCT_ID);

        verify(repository).save(productCaptor.capture());

        Product actual = productCaptor.getValue();

        assertEquals(FIXED_USD_EXCHANGE_RATE.multiply(PRICE_EUR),
                actual.getPriceUsd());
    }

    @Test
    void findAll() {
        Sort sort = Sort.by(SORT_FIELD);
        Pageable pageable = PageRequest.of(PAGE_NUM - 1 ,PAGE_SIZE, sort);
        Page<Product> page = new PageImpl<>(List.of(constructEntity()));

        given(repository.findAll(pageable)).willReturn(page);

        Page<Product> actual = service.findAll(PAGE_NUM, PAGE_SIZE, SORT_FIELD, SORT_DIRECTION);

        assertEquals(page, actual);
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
}