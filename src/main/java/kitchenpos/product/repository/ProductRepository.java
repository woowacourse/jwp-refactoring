package kitchenpos.product.repository;

import java.util.List;
import kitchenpos.product.domain.Product;

public interface ProductRepository {
    Product save(Product entity);

    Product findById(Long id);

    List<Product> findAll();
}
