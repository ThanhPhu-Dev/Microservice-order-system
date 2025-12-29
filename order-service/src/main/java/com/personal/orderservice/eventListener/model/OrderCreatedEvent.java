package com.personal.orderservice.eventListener.model;

import com.personal.common.Enum.MessageType;
import com.personal.common.dto.BaseEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderCreatedEvent extends BaseEvent {
    private Integer orderId;

    public OrderCreatedEvent(MessageType messageType, Integer id, String traceId) {
        super(messageType, traceId);
        this.orderId = id;
    }
}
