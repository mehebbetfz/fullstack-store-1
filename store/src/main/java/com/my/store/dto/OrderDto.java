package com.my.store.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderDto {
    @NotBlank(message = "Адрес доставки обязателен")
    private String shippingAddress;

    @NotBlank(message = "Телефон обязателен")
    private String phone;

    private String notes;
}
