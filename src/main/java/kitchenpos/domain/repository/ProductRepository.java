package kitchenpos.domain.repository;

import kitchenpos.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findById(Long aLong);

    List<Product> findAll();
}
