package kitchenpos.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;

public interface ProductRepository {

  Product save(Product product);

  Optional<Product> findById(Long id);

  List<Product> findAll();
}
