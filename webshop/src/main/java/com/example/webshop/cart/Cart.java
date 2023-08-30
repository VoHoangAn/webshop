/*
package com.example.webshop.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//@Data
//@Builder
@Entity
@NoArgsConstructor
@Getter
@Setter
//@AllArgsConstructor
//@Table(name = "_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true,nullable = false)
    private String name;
    private String image;
    @JsonIgnore
    private Long createdBy;
    @JsonIgnore
    private boolean isAvailable = true;
    @CreationTimestamp
    @JsonIgnore
    private Timestamp timestamp;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,orphanRemoval = true)
//    @JsonIgnore
//    @ToString.Exclude
//    @JoinTable(name = "CATEGORY_MAPPING", joinColumns = @JoinColumn(name = "CATEGORY_ID"),
//            inverseJoinColumns = @JoinColumn(name = "CATEGORYFIELD_ID",updatable = false,insertable = false))
    private List<CategoryField> categoryFields = new ArrayList<>();

    public void addCategoryFields(CategoryField categoryField) {
        this.categoryFields.add(categoryField);
        categoryField.setCategory(this);
    }

    public void removeCategoryFields(CategoryField categoryField) {
        categoryField.setCategory(null);
        this.categoryFields.remove(categoryField);
    }
}
*/

package com.example.webshop.cart;

import com.example.webshop.product.Product;
import com.example.webshop.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

//@Data
//@Builder
@Entity
@NoArgsConstructor
@Getter
@Setter
//@AllArgsConstructor
//@Table(name = "_category")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id",insertable = false,updatable = false)
//    @JsonIgnoreProperties({"name","image","category","quantity","price","sale","soldOut",
//        "description","color","brand","releaseYear","guaranteePeriod","size","resolution","refreshRate"})
    private Product product;
    @Column(name = "product_id")
    private Long productId;
    @Column(nullable = false)
    private String productName;
    private Long quantity;
    private BigDecimal price;
    private float sale;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id",insertable = false,updatable = false)
    private User user;
    @Column(name = "user_id")
    private Long userId;
    @CreationTimestamp
    @JsonIgnore
    @Column(updatable = false)
    private Timestamp timestamp;
}



