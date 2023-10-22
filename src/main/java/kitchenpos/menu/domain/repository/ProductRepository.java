package kitchenpos.menu.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Product;

public interface ProductRepository {

  Product save(final Product entity);

  Optional<Product> findById(final Long id);

  List<Product> findAll();
}
