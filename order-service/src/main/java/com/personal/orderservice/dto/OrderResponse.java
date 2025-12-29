package com.personal.orderservice.dto;

import com.personal.common.Enum.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Integer orderId;
    private Status status;
}
