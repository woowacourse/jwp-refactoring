package kitchenpos.product.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.product.domain.Product;

public interface ProductDao {

    Long save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();
}
