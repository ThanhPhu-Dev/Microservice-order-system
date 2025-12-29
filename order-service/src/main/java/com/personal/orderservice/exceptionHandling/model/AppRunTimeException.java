package com.personal.orderservice.exceptionHandling.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class AppRunTimeException extends Exception{
    private String message;
    private Integer code;
    private String traceId;
}
