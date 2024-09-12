package com.storeManagementTool.StoreManagementTool.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(Exception ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), new Date());
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({IllegalArgumentException.class, InsufficientQuantityException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(Exception ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), new Date());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
