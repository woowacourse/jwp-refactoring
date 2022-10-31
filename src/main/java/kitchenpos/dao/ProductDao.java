package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.product.Product;

public interface ProductDao {

    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();
}
