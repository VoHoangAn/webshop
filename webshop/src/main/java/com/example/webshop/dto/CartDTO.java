package com.example.webshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDTO {
    private Long id;
    private Long productId;
    private Long quantity;
}
