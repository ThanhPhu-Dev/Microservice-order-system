package com.personal.orderservice.service;

import com.personal.orderservice.dto.OrderDto;

public interface OrderServiceInterface {
    Integer createOrder(OrderDto orderDto, String traceId);
}
