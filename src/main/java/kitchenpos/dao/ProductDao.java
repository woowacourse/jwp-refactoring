package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;

public interface ProductDao {

    Long save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();
}
