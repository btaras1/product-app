package com.product.app.mapper;

import com.product.app.model.Product;
import com.product.app.rest.dto.request.ProductRequestDto;
import com.product.app.rest.dto.response.ProductResponseDto;
import com.product.app.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class ProductMapper {

    CurrencyService currencyService;

    public Product toEntity(ProductRequestDto requestDto) {
        return Product.builder()
                .code(requestDto.getCode())
                .name(requestDto.getName())
                .priceEur(requestDto.getPriceEur())
                .priceUsd(currencyService.convertEurToUsd(requestDto.getPriceEur()))
                .isAvailable(requestDto.isAvailable())
                .build();
    }

    public ProductResponseDto toResponse(Product entity) {
        return ProductResponseDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .priceEur(entity.getPriceEur())
                .priceUsd(entity.getPriceUsd())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .available(entity.isAvailable())
                .build();
    }
}
