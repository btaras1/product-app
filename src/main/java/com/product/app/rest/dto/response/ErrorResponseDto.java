package com.product.app.rest.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class ErrorResponseDto {

    int statusCode;
    String status;
    String path;
    String message;
}
