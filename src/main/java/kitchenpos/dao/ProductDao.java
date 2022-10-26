package kitchenpos.dao;

import kitchenpos.domain.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    ProductDto save(ProductDto entity);

    Optional<ProductDto> findById(Long id);

    List<ProductDto> findAll();
}
