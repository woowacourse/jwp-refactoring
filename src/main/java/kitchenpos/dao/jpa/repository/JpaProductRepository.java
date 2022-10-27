package kitchenpos.dao.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import kitchenpos.domain.Product;

public interface JpaProductRepository extends Repository<Product, Long> {

    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();

}
