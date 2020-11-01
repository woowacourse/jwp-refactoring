package kitchenpos.domain.repository;

import java.util.List;
import java.util.Optional;

import kitchenpos.domain.entity.Product;

public interface ProductRepository {
    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();
}
