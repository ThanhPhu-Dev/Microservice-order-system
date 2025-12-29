package com.personal.orderservice.controller;

import com.personal.orderservice.dto.OrderDto;
import com.personal.orderservice.dto.OrderResponse;
import com.personal.orderservice.exceptionHandling.model.AppRunTimeException;
import com.personal.orderservice.service.OrderServiceInterface;
import com.personal.common.Enum.Status;
import com.personal.common.util.JsonMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceInterface orderServiceInterface;
    private final JsonMapper mapper;

    @GetMapping
    public String ok() {
        return "OK";
    }

    @PostMapping("/createOrder")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderDto orderRequest, BindingResult bindingResult) throws BindException {
        String traceId = System.currentTimeMillis() + "_InitOrder";

        if (bindingResult.hasErrors()){
            log.error("{} Form is invalid: {}", traceId, bindingResult.getAllErrors().get(0).getDefaultMessage());
            throw new BindException(bindingResult);
        }
        log.info("{} Request data {}", traceId, mapper.stringify(orderRequest, traceId));
        Integer orderId = orderServiceInterface.createOrder(orderRequest, traceId);
        return ResponseEntity.accepted().body(new OrderResponse(orderId, Status.CREATE));
    }
}
