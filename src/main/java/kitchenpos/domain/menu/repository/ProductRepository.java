package kitchenpos.domain.menu.repository;


import kitchenpos.domain.menu.Product;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends Repository<Product, Long> {

    Product save(final Product product);

    List<Product> findAll();

    Optional<Product> findById(final Long productId);
}
