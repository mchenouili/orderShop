package be.chencorp.shop.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    boolean isProd = false;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class ErrorResource{
        String code;
        String message;
        String stackTrace;
    }

    private ErrorResource convertException(MicroServiceException e){
        String stackTrace = null;
        if (!isProd){
            stackTrace = ExceptionUtils.getStackTrace(e);
        }
        return ErrorResource.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .stackTrace(stackTrace)
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequest(
            BadRequestException ex, WebRequest request) {
        return handleExceptionInternal(ex, convertException(ex),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, convertException(ex),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}