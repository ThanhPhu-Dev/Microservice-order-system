package com.personal.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JsonMapper {
    private final ObjectMapper mapper;

    public String stringify(Object data, String traceId){
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.info("{} Cannot Parse Object to Json. ERROR:: {}.", traceId, e.getMessage());
            return "Cannot Parse Object";
        }
    }


}
