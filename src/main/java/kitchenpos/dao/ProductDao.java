package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product.Product;
import org.springframework.data.repository.Repository;

public interface ProductDao extends Repository<Product, Long> {

    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();
}
