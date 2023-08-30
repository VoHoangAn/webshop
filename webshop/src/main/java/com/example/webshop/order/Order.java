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

package com.example.webshop.order;

import com.example.webshop.address.Address;
import com.example.webshop.orderDetail.OrderDetail;
import com.example.webshop.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@ToString

//@AllArgsConstructor
@Table(name = "_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "order",cascade = CascadeType.REMOVE)
    @Column(nullable = false)
    private List<OrderDetail> orderDetails;
    @Column(nullable = false)
    private PaymentOption paymentOption;
    @Column(unique = true)
    private String zaloTransactionId;
    @Column(nullable = false)
    private BigDecimal totalAmount;
    @Enumerated
    private Status status = Status.PAYING;
    private Date orderedTime;
    private Date shippedTime;
    private Date deliveredTime;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ordered_by",insertable = false,updatable = false)
    private User user;
    @Column(name = "ordered_by")
    private Long userId;
    private String userName;
    private String address;
    private String mac;
    @CreationTimestamp
    @JsonIgnore
    @Column(updatable = false)
    private Timestamp createsTime;
}



