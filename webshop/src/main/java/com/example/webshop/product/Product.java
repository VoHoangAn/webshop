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

package com.example.webshop.product;

import com.example.webshop.user.User;
import com.example.webshop.util.ProductSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

//@Data
//@Builder
@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
//@AllArgsConstructor
//@Table(name = "_category")
@JsonSerialize(using = ProductSerializer.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonValue
    private Long id;
    @Column(unique = true,nullable = false)
    private String name;
    private String image;
    @Enumerated(EnumType.STRING)
    private Category category;
    private Long quantity;
    private BigDecimal price;
    private float sale;
    @Column(nullable = true)
    private boolean soldOut;
    private String description;
//    @Convert(converter= StringToListConverter.class)
    private String color;
    private String brand;
    private int releaseYear;
    private String guaranteePeriod;
    private String size;
    private String resolution;
    private String refreshRate;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "created_by",insertable = false,updatable = false)
    private User user;
    @Column(name = "created_by")
    private Long userId;
    @JsonIgnore
    private boolean isAvailable = true;
    @CreationTimestamp
    @JsonIgnore
    private Timestamp createTime;
    @UpdateTimestamp
    @JsonIgnore
    private Timestamp updateTime;

}



