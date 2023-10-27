package kitchenpos.product.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

public interface ProductDao {

  ProductEntity save(ProductEntity entity);

  Optional<ProductEntity> findById(Long id);

  List<ProductEntity> findAll();
}
