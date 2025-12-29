package com.personal.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class productionDto {
    @NotEmpty(message = "productionId is missing")
    private String productId;
    @Min(value = 0, message = "quantity must greater than 0")
    private Integer quantity;
}
