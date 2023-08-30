package com.example.webshop.cart;

import com.example.webshop.baseRepository.BaseRepositoryImpl;
import com.example.webshop.user.User;
import com.querydsl.core.Tuple;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public class CartRepositoryImpl extends BaseRepositoryImpl<Cart, Long>
         {

    public CartRepositoryImpl(EntityManager em) {
        super(Cart.class, em);
    }

    public Optional<List<Cart>> findAllByUserId(Long id){
        return Optional.ofNullable(queryFactory.selectFrom(cart)
                .where(cart.userId.eq(id)).fetch());
    }

     @Transactional
     public void add(List<Cart> newCart) {
         for (Cart item:newCart) {
             em.persist(item);
         }
         em.flush();
     }

     public final Logger l = LoggerFactory.getLogger(CartRepositoryImpl.class);
    @Transactional
    public void update(List<Cart> newCart, Long userId) {
        for (Cart item:newCart) {
            if(item.getUser()==null) item.setUserId(userId);
            if(item.getId()==null) {
                em.persist(item);
            } else {
                em.merge(item);
            }
        }
        em.flush();
//        return queryFactory.update(cart)
//                .set(cart.quantity,newItem.getQuantity())
//                .set(cart.price, newItem.getPrice())
//                .set(cart.sale,newItem.getSale())
//                .where(cart.productId.eq(newItem.getProductId()))
//                .execute();
    }

     @Transactional
     public void deleteAllByUserId(Long id) {
        queryFactory.delete(cart).where(cart.userId.eq(id)).execute();
     }

     public List<Tuple> findAllProductIdsAndQuantityInCartByUserId(Long id) {
         return queryFactory.select(cart.productId,cart.quantity)
                 .from(cart).where(cart.userId.eq(id)).fetch();
     }

}
