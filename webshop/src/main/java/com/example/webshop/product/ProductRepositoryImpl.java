package com.example.webshop.product;

import com.example.webshop.address.QAddress;
import com.example.webshop.baseRepository.BaseRepositoryImpl;
import com.example.webshop.cart.QCart;
import com.example.webshop.order.QOrder;
import com.example.webshop.orderDetail.OrderDetail;
import com.example.webshop.orderDetail.QOrderDetail;
import com.example.webshop.user.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.util.TypeSafeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public class ProductRepositoryImpl extends BaseRepositoryImpl<Product, Long> {
    @Lazy
    @Autowired
    private ProductRepository productRepositoryQuerydsl;

    public ProductRepositoryImpl(EntityManager em) {
        super(Product.class, em);
    }

    public Predicate isProductAvailable() {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(product.isAvailable.eq(true).and(product.soldOut.eq(false)));
    }
//    public Predicate hasPrivilege() {
//        BooleanBuilder booleanBuilder = new BooleanBuilder();
//        return booleanBuilder.and(product.isAvailable.eq(true).and(product.soldOut.eq(false)));
//    }

    public boolean existsByName(String name) {
        return queryFactory.select(Wildcard.count).from(product)
                .where(product.name.eq(name).and(isProductAvailable())).fetch().get(0)>0;
    }

    public Optional<Product> findById(Long id){
        return Optional.ofNullable(queryFactory.selectFrom(product)
                .where(product.id.eq(id).and(isProductAvailable())).fetchFirst());
    }

    public List<Product> findAllAvailableProduct() {
        return queryFactory.selectFrom(product).where(isProductAvailable()).fetch();
    }

    public List<Product> findSomeByIdsOrderById(List<Long> ids) {
        return queryFactory.selectFrom(product).where(product.id.in(ids).and(isProductAvailable()))
                .orderBy(product.id.asc()).fetch();

    }

    public long update(Product newProduct) {
        return queryFactory.update(product)
                .set(product.image, newProduct.getImage())
                .set(product.price, newProduct.getPrice())
                .set(product.quantity,newProduct.getQuantity())
                .set(product.sale, newProduct.getSale())
                .set(product.soldOut,newProduct.isSoldOut())
                .set(product.category, newProduct.getCategory())
                .set(product.description, newProduct.getDescription())
                .set(product.color, newProduct.getColor())
                .set(product.brand, newProduct.getBrand())
                .set(product.releaseYear, newProduct.getReleaseYear())
                .set(product.guaranteePeriod, newProduct.getSize())
                .set(product.resolution, newProduct.getResolution())
                .set(product.refreshRate, newProduct.getRefreshRate())
                .where(product.id.eq(newProduct.getId()))
                .execute();
    }

    public List<Tuple> findQuantityOfSomeProducts(List<Long> productIds) {
        return queryFactory.select(product.id,product.quantity).from(product).where(product.id.in(productIds)).fetch();
    }

    @Transactional
    public void calculateQuantityByOrderItems(List<OrderDetail> orderDetails) {
        for (OrderDetail orderItem:orderDetails) {
            Long curProduct = queryFactory.select(product.quantity).from(product)
                    .where(product.id.eq(orderItem.getProductId())).fetchFirst();
            queryFactory.update(product).set(product.quantity,curProduct-orderItem.getQuantity())
                    .where(product.id.eq(orderItem.getProductId())).execute();
        }
    }

     public void updateSoldOutById(Long id) {
         queryFactory.update(product).set(product.soldOut,true).where(product.id.eq(id)).execute();
     }

         public Page<Product> find(int page, int size, Map<String,String> filterCriteria, Map<String,String> sort) {
             Sort sortAble = sortBuilder(sort);
             Pageable pageable = PageRequest.of(page-1,size,sortAble);
             BooleanBuilder criteria = criteriaBuilder(filterCriteria);
             return productRepositoryQuerydsl.findAll(criteria,pageable);
         }

         private Sort sortBuilder(Map<String,String> sort) {
             Sort orders = Sort.unsorted();

             for (Map.Entry<String,String> entry:sort.entrySet()) {
                 if(entry.getValue().equals("asc")) {
                     orders = orders.and(Sort.by(entry.getKey()).ascending());
                 } else if (entry.getValue().equals("desc")) {
                     orders = orders.and(Sort.by(entry.getKey()).descending());
                 }
             }
             return orders;
         }

         private static Logger l = LoggerFactory.getLogger(ProductRepositoryImpl.class);
         private BooleanBuilder criteriaBuilder(Map<String,String> filterCriteria) {
             BooleanBuilder booleanBuilder = new BooleanBuilder();
             for (Map.Entry<String,String> entry:filterCriteria.entrySet()) {
                 if (entry.getValue().isEmpty()) continue;
                 List<String> listVal = new ArrayList<>();
                 boolean b = entry.getValue().contains(",") ? listVal.addAll(List.of(entry.getValue().split(","))) : listVal.add(entry.getValue());
                 if(entry.getKey()=="price") {
                     booleanBuilder.and(product.price.between(Integer.parseInt(listVal.get(0)),
                             Integer.parseInt(listVal.get(1))));
                 } else if (entry.getKey()=="category") {
                     booleanBuilder.and(product.category.in(listVal.stream().map(i->Category.valueOf(i)).toList()));
                 } else if (entry.getKey()=="sale") {
                     booleanBuilder.and(product.sale.in((Number) listVal));
                 } else if (entry.getKey()=="color") {
                     booleanBuilder.and(product.color.in(listVal));
                 } else if (entry.getKey()=="brand") {
                     booleanBuilder.and(product.brand.in(listVal));
                 } else if (entry.getKey()=="size") {
                     booleanBuilder.and(product.size.in(listVal));
                 } else if (entry.getKey()=="resolution") {
                     booleanBuilder.and(product.resolution.in(listVal));
                 } else if (entry.getKey()=="refreshRate") {
                     booleanBuilder.and(product.refreshRate.in(listVal));
                 }
             }
             l.error(booleanBuilder.toString());
             return booleanBuilder;
         }
     }
