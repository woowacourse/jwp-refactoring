package kitchenpos.product.persistence;

import java.util.List;
import java.util.Optional;
import kitchenpos.product.application.dto.ProductPersistence;

public interface ProductDao {

  ProductPersistence save(ProductPersistence entity);

  Optional<ProductPersistence> findById(Long id);

  List<ProductPersistence> findAll();
}
