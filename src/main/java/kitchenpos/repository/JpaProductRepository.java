package kitchenpos.repository;

import kitchenpos.domain.product.Product;
import org.springframework.data.repository.Repository;

public interface JpaProductRepository extends Repository<Product, Long>, ProductRepository {
}
