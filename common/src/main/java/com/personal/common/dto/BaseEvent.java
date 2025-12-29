package com.personal.common.dto;

import com.personal.common.Enum.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseEvent {
    private MessageType messageType;
    private String traceId;
}
