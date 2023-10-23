package kitchenpos.repository;

import kitchenpos.domain.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findById(Long aLong);
    
    List<Product> findAll();
}
