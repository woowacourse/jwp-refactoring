package kitchenpos.product.persistence;

import java.util.List;
import java.util.Optional;
import kitchenpos.product.domain.Product;

public interface ProductDao {

  Product save(Product entity);

  Optional<Product> findById(Long id);

  List<Product> findAll();
}
