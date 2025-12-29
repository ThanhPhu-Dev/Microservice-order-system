package com.personal.orderservice.service;

import com.personal.orderservice.dto.OrderDto;
import com.personal.orderservice.eventListener.model.OrderCreatedEvent;
import com.personal.orderservice.model.Order;
import com.personal.orderservice.repository.OrderRepository;
import com.personal.common.Enum.MessageType;
import com.personal.common.Enum.Status;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderServiceImpl implements OrderServiceInterface{

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Integer createOrder(OrderDto orderDto, String traceId){
        Order order = new Order();
        order.setStatus(Status.CREATE);
        order.setUserId(orderDto.getUserId());
        order.setTotalPrice(BigDecimal.ONE);
        this.orderRepository.save(order);
        applicationEventPublisher.publishEvent( new OrderCreatedEvent(MessageType.OrderCreated, order.getId(), traceId));
        return order.getId();
    }
}
