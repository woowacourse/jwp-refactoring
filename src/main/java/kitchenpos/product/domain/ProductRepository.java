package kitchenpos.product.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ProductRepository extends Repository<Product, Long> {

    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();

}
