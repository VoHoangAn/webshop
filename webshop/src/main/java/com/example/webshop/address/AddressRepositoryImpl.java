package com.example.webshop.address;

import com.example.webshop.baseRepository.BaseRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class AddressRepositoryImpl extends BaseRepositoryImpl<Address,Long> {
    public AddressRepositoryImpl(EntityManager em) {
        super(Address.class, em);
    }

    public List<Address> findAllByUserId(Long id) {
        return queryFactory.selectFrom(address).where(address.user.id.eq(id)).fetch();
    }
}
