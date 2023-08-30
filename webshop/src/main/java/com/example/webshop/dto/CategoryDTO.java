package com.example.webshop.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String image;
    private Long createdBy;
}
