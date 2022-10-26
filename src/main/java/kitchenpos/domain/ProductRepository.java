package kitchenpos.domain;

import kitchenpos.domain.menu.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
