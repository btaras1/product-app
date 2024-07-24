package com.product.app.rest.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@FieldDefaults(makeFinal = true, level = PRIVATE)
@Valid
public class ProductRequestDto {

    @NotEmpty(message = "Product code cannot be empty.")
    @Size(min = 10, max = 10, message = "Product code needs to have 10 characters.")
    String code;
    @NotEmpty(message = "Product name cannot be empty.")
    String name;
    @NotNull(message = "Price in EUR is required.")
    BigDecimal priceEur;
    boolean available;
}
