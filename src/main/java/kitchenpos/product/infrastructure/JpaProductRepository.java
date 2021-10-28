package kitchenpos.product.infrastructure;

import kitchenpos.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<Product, Long> {

}
