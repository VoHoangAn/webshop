package com.example.webshop.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product,Long>, QuerydslPredicateExecutor<Product> {
//    boolean existsByName(String name);
//    @Modifying
//    @Query("""
//        update Product p set p.isAvailable=false where p.name=?1
//    """)
//    int removeCategory(String name);
//    @Modifying
//    @Query("""
//        update Product p set p.name=?2, p.image=?3 where p.id=?1
//    """)
//    int update(Long id, String name,String img, String desciption, String color, String brand,
//               String releaseYear, String guaranteePeriod, String size, String resolution, String refreshRate);
//    boolean existsByCreatedBy(Long id);
//    boolean existsByImage(String img);


}



