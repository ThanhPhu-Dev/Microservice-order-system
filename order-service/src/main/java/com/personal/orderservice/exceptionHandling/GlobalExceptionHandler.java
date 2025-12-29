package com.personal.orderservice.exceptionHandling;

import com.personal.orderservice.exceptionHandling.model.AppRunTimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler({ ExceptionA.class, ExceptionB.class })
//    public ResponseEntity<String> handleExceptionA(Exception e) {
//        return ResponseEntity.status(432).body(e.getMessage());
//    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBindException(BindException e) {
        String errorMessage = "Request is invalid";
        log.error("Request is invalid");
        if (e.getBindingResult().hasErrors())
            e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return errorMessage;
    }

    @ExceptionHandler(AppRunTimeException.class)
    public ResponseEntity<AppRunTimeException> handleUnwantedException(AppRunTimeException e) {
       log.error("{} response error {} ", e.getTraceId(), "Create Order fail", e.getCode(), e);
        return ResponseEntity.status(500).body(e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnwantedException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Unknow error");
    }
}
