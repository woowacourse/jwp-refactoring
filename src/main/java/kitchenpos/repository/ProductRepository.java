package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Override
    List<Product> findAll();
}
