package kitchenpos.core.domain.product.repository;

import kitchenpos.core.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsById(Long productId);
}
