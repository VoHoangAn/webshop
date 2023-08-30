package com.example.webshop.user;

import com.example.webshop.baseRepository.BaseRepositoryImpl;
import com.example.webshop.order.Order;
import com.querydsl.core.types.dsl.Wildcard;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<User, Long> {
    public UserRepositoryImpl(EntityManager em) {
        super(User.class, em);
    }

    public Optional<User> findByEmail(String email){
        return Optional.ofNullable(queryFactory.selectFrom(user).where(user.email.eq(email)).fetchFirst());
    }

    public long enableUser(String email) {
        return queryFactory.update(user).set(user.enabled,true).where(user.email.eq(email)).execute();
    }

    public User findByUser(Long id){
        return queryFactory.selectFrom(user).where(user.id.eq(id)).fetchFirst();
    }
    public boolean existsUserByEmail(String email){
        return queryFactory.select(Wildcard.count).from(user)
                .where(user.email.eq(email).and(user.enabled.eq(true))).fetch().get(0)>0;
    }

    @Transactional
    public long changeUserStateToPaying(Long id){
        return queryFactory.update(user).set(user.role,Role.ROLE_PAYING).where(user.id.eq(id)).execute();
    }

    @Transactional
    public long changeUserStateBackToUser(Long id){
        return queryFactory.update(user).set(user.role,Role.ROLE_USER).where(user.id.eq(id)).execute();
    }
}
