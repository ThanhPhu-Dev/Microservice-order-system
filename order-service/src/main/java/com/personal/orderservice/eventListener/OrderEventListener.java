package com.personal.orderservice.eventListener;

import com.personal.orderservice.eventListener.model.OrderCreatedEvent;
import com.personal.orderservice.exceptionHandling.model.AppRunTimeException;
import com.personal.common.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class OrderEventListener {

    @Autowired
    private JsonMapper jsonMapper;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderEvent(OrderCreatedEvent orderCreatedEvent) throws AppRunTimeException {
        //kafka
        log.info("{} event publish: {}", orderCreatedEvent.getTraceId(), jsonMapper.stringify(orderCreatedEvent, orderCreatedEvent.getTraceId()));
    }
}
