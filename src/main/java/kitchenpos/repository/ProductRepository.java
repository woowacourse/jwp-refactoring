package kitchenpos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import kitchenpos.domain.Product;

public interface ProductRepository extends Repository<Product, Long> {

    Product save(Product product);

    List<Product> findAll();
    Optional<Product> findById(Long id);
}
