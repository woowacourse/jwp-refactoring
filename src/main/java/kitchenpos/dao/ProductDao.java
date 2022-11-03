package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.product.Product;

public interface ProductDao {
    Product save(Product entity);

    Product findById(Long id);

    List<Product> findAll();
}
