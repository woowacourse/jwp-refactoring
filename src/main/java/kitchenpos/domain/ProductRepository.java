package kitchenpos.domain;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

  Product2 save(Product2 product2);

  Optional<Product2> findById(Long id);

  List<Product2> findAll();
}
