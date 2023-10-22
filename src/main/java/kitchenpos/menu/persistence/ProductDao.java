package kitchenpos.menu.persistence;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.application.entity.ProductEntity;

public interface ProductDao {

  ProductEntity save(ProductEntity entity);

  Optional<ProductEntity> findById(Long id);

  List<ProductEntity> findAll();
}
