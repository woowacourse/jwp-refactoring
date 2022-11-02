package kitchenpos.product.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Products;

public interface ProductDao {

    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    Products findAllByIdIn(List<Long> ids);
}
