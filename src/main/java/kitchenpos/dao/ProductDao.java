package kitchenpos.dao;

import kitchenpos.domain.product.Product;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductDao extends Repository<Product, Long> {
    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();
}
