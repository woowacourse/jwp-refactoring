package kitchenpos.product.repository;

import kitchenpos.product.doamin.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
