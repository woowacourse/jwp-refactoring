package kitchenpos.product.persistence;

import kitchenpos.product.persistence.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductDao {

    ProductEntity save(ProductEntity entity);

    Optional<ProductEntity> findById(Long id);

    List<ProductEntity> findAll();
}
