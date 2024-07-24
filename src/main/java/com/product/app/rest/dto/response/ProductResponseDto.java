package com.product.app.rest.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class ProductResponseDto {

    UUID id;
    String code;
    String name;
    BigDecimal priceEur;
    BigDecimal priceUsd;
    boolean available;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
