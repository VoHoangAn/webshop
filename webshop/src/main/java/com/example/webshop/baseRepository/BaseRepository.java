package com.example.webshop.baseRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T,ID> extends JpaRepository<T, ID> {
    void clear();

    void detach(T entity);
}
