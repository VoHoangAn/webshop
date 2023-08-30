package com.example.webshop.baseRepository;

import com.example.webshop.address.QAddress;
import com.example.webshop.cart.QCart;
import com.example.webshop.order.QOrder;
import com.example.webshop.orderDetail.QOrderDetail;
import com.example.webshop.product.Product;
import com.example.webshop.product.QProduct;
import com.example.webshop.user.QUser;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public abstract class BaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID>
        implements BaseRepository<T, ID> {
    protected final QProduct product = QProduct.product;
    protected final QAddress address = QAddress.address1;
    protected final QCart cart = QCart.cart;
    protected final QOrder order = QOrder.order;
    protected final QOrderDetail orderDetail = QOrderDetail.orderDetail;
    protected final QUser user = QUser.user;
    @PersistenceContext
    protected final EntityManager em;
    protected final JPAQueryFactory queryFactory;
    public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public void clear() {
em.clear();
    }

    @Override
    public void detach(T entity) {
em.detach(entity);
    }
}
