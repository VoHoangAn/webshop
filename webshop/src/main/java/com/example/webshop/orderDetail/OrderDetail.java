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

package com.example.webshop.orderDetail;

import com.example.webshop.order.Order;
import com.example.webshop.order.PaymentOption;
import com.example.webshop.order.Status;
import com.example.webshop.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

//@Data
//@Builder
@Entity
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
@ToString
//@AllArgsConstructor
//@Table(name = "_category")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @FieldNameConstants.Exclude
    private Long id;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id",insertable = false,updatable = false)
    @FieldNameConstants.Exclude
    @ToString.Exclude
    private Product product;
    @Column(name = "product_id")
    private Long productId;
    @Column(nullable = false)
    private String productName;
    private Long quantity;
    private BigDecimal price;
    private float sale;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JsonIgnore
    @JoinColumn(name = "order_id",insertable = false,updatable = false)
    @FieldNameConstants.Exclude
    @ToString.Exclude
    private Order order;
    @Column(name = "order_id")
    private Long orderId;
}



