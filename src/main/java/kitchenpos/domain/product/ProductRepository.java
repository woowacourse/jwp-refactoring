package kitchenpos.domain.product;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends Repository<Product, Long> {

    Product save(Product product);

    List<Product> findAll();

    Optional<Product> findById(Long id);

    List<Product> findAllByIdIn(List<Long> ids);
}
