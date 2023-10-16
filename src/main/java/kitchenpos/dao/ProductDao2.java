package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.entity.ProductEntity;

public interface ProductDao2 {
    ProductEntity save(ProductEntity entity);

    Optional<ProductEntity> findById(Long id);

    List<ProductEntity> findAll();
}
