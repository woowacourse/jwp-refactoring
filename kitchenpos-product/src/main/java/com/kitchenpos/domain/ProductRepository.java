package com.kitchenpos.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends Repository<Product, Long> {

    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    @Query("SELECT SUM(p.price) FROM Product p WHERE p.id IN :productIds")
    Price sumPricesByIdIn(@Param("productIds") List<Long> productIds);

    List<Product> findAllByIdIn(List<Long> productIds);

    long countAllByIdIn(List<Long> ids);
}
