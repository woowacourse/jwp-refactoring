package kitchenpos.legacy.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.legacy.domain.Product;

public interface ProductDao {

    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();
}
