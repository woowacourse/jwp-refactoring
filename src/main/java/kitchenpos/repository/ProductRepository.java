package kitchenpos.repository;

import kitchenpos.domain.productquantity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
