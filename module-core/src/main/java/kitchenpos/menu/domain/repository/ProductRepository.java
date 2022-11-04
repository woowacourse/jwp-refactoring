package kitchenpos.menu.domain.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import kitchenpos.menu.domain.Product;

public interface ProductRepository extends Repository<Product, Long> {

    Product save(final Product product);

    List<Product> findAll();

    Optional<Product> findById(final Long productId);
}
