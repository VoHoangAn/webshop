package com.example.webshop.order;

import com.example.webshop.baseRepository.BaseRepositoryImpl;
import com.example.webshop.cart.Cart;
import com.example.webshop.cart.CartRepositoryImpl;
import com.example.webshop.orderDetail.OrderDetailRepositoryImpl;
import com.example.webshop.product.Product;
import com.example.webshop.product.ProductRepositoryImpl;
import com.example.webshop.user.User;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.NumberExpression;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl extends BaseRepositoryImpl<Order, Long> {

    public OrderRepositoryImpl(EntityManager em) {
        super(Order.class, em);
    }

    public Optional<List<Order>> findAllByUserId(Long id){
        return Optional.ofNullable(queryFactory.selectFrom(order)
                .where(order.userId.eq(id)).fetch());
    }

//     @Transactional
     public Order create(Order newOrder) {
         em.persist(newOrder);
         em.flush();
         return newOrder;
//         NumberExpression n = new NumberExpression();
//         queryFactory.update(orderDetail).set(orderDetail.quantity, .)
     }

     //checkInvalidBuyAmount,getDetail,zalopaymnt,getStatus
             // checkUserHasBoughtProduct

     public final Logger l = LoggerFactory.getLogger(CartRepositoryImpl.class);
    @Transactional
    public void updateStatus(List<Cart> newCart, Long userId) {

     }

     public Order findByMac(String mac) {
        return queryFactory.selectFrom(order).where(order.mac.eq(mac)).fetchFirst();
     }

     @Transactional
    public void changeOrderStatusToOrdered(Long id){
        queryFactory.update(order).set(order.status,Status.ORDERED).where(order.id.eq(id)).execute();
    };

}
