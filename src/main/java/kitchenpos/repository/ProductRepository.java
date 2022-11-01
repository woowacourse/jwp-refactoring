package kitchenpos.repository;

import java.util.Collection;
import java.util.List;
import kitchenpos.domain.Product;
import org.springframework.data.repository.Repository;

public interface ProductRepository extends Repository<Product, Long> {
    Product save(Product entity);

    List<Product> findAll();

    List<Product> findByIdIn(Collection<Long> productIds);
}
