package com.example.webshop.orderDetail;

import com.example.webshop.baseRepository.BaseRepositoryImpl;
import com.example.webshop.cart.Cart;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository
public class OrderDetailRepositoryImpl extends BaseRepositoryImpl<OrderDetail,Long> {
    public OrderDetailRepositoryImpl(EntityManager em) {
        super(OrderDetail.class, em);
    }

    @Transactional
    public void saveAll(List<OrderDetail> orderDetails) {
        for (OrderDetail item:orderDetails) {
            em.persist(item);
        }
        em.flush();
    }

    public Optional<List<OrderDetail>> findAllByOrderId(Long id){
        return Optional.ofNullable(queryFactory.selectFrom(orderDetail)
                .where(orderDetail.order.id.eq(id)).fetch());
    }

}
