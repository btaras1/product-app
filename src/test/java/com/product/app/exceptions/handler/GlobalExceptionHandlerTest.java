package com.product.app.exceptions.handler;

import com.product.app.exceptions.BadRequestException;
import com.product.app.exceptions.NotFoundException;
import com.product.app.rest.dto.response.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private static final String BAD_REQUEST_EXCEPTION_MESSAGE = "EUR price of a product must be supplied and greater than zero.";
    private static final String INTEGRITY_VIOLATION_EXCEPTION_MESSAGE = "ERROR: duplicate key value violates unique constraint product_code_key";
    private static final String NOT_FOUND_EXCEPTION_MESSAGE = "Product for the given id : %s  was not found.";
    private static final String REQUEST_URI = "/product/save";
    @Mock
    private HttpServletRequest httpServletRequest;

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void badRequestException() {
        BadRequestException ex = new BadRequestException(BAD_REQUEST_EXCEPTION_MESSAGE);
        HttpStatus httpStatus = BAD_REQUEST;

        given(httpServletRequest.getRequestURI()).willReturn(REQUEST_URI);

        ModelAndView actual = globalExceptionHandler.badRequestException(ex, httpServletRequest);
        ErrorResponseDto actualResponse = (ErrorResponseDto) actual.getModel().get("exception");

        assertEquals(actual.getViewName(), "custom_error");
        assertEquals(actualResponse.getMessage(), ex.getMessage());
        assertEquals(actualResponse.getPath(), REQUEST_URI);
        assertEquals(actualResponse.getStatus(), httpStatus.getReasonPhrase());
        assertEquals(actualResponse.getStatusCode(), httpStatus.value());
    }

    @Test
    void dataIntegrityViolationException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException(INTEGRITY_VIOLATION_EXCEPTION_MESSAGE);
        HttpStatus httpStatus = BAD_REQUEST;

        given(httpServletRequest.getRequestURI()).willReturn(REQUEST_URI);

        ModelAndView actual = globalExceptionHandler.dataIntegrityViolationException(ex, httpServletRequest);
        ErrorResponseDto actualResponse = (ErrorResponseDto) actual.getModel().get("exception");

        assertEquals(actual.getViewName(), "custom_error");
        assertEquals(actualResponse.getMessage(), ex.getMessage());
        assertEquals(actualResponse.getPath(), REQUEST_URI);
        assertEquals(actualResponse.getStatus(), httpStatus.getReasonPhrase());
        assertEquals(actualResponse.getStatusCode(), httpStatus.value());
    }

    @Test
    void notFoundException() {
        NotFoundException ex = new NotFoundException(NOT_FOUND_EXCEPTION_MESSAGE);
        HttpStatus httpStatus = NOT_FOUND;

        given(httpServletRequest.getRequestURI()).willReturn(REQUEST_URI);

        ModelAndView actual = globalExceptionHandler.notFoundException(ex, httpServletRequest);
        ErrorResponseDto actualResponse = (ErrorResponseDto) actual.getModel().get("exception");

        assertEquals(actual.getViewName(), "custom_error");
        assertEquals(actualResponse.getMessage(), ex.getMessage());
        assertEquals(actualResponse.getPath(), REQUEST_URI);
        assertEquals(actualResponse.getStatus(), httpStatus.getReasonPhrase());
        assertEquals(actualResponse.getStatusCode(), httpStatus.value());
    }
}