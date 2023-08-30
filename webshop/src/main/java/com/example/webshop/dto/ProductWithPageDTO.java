package com.example.webshop.dto;

import com.example.webshop.product.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProductWithPageDTO {
    private List<Product> products;
    private int currentPage;
    private int size;
    private int totalPage;
}
