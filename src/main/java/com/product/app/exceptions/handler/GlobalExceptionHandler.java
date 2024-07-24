package com.product.app.exceptions.handler;

import com.product.app.exceptions.BadRequestException;
import com.product.app.exceptions.NotFoundException;
import com.product.app.rest.dto.response.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ModelAndView badRequestException(BadRequestException ex, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("custom_error");
        mv.addObject("exception", constructErrorResponse(ex, request, BAD_REQUEST));
        return mv;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView dataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("custom_error");
        mv.addObject("exception", constructErrorResponse(ex, request, BAD_REQUEST));
        return mv;
    }

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView notFoundException(NotFoundException ex, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("custom_error");
        mv.addObject("exception", constructErrorResponse(ex, request, NOT_FOUND));
        return mv;
    }

    private ErrorResponseDto constructErrorResponse(Exception ex, HttpServletRequest request, HttpStatus status) {
        return ErrorResponseDto.builder()
                .status(status.getReasonPhrase())
                .statusCode(status.value())
                .path(request.getRequestURI())
                .message(ex.getMessage())
                .build();
    }
}
