package kitchenpos.dao;

import kitchenpos.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {

    Product save(final Product entity);

    Optional<Product> findById(final Long id);

    List<Product> findAll();

    default Product getById(final Long id) {
        return findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
