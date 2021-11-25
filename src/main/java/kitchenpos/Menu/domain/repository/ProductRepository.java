package kitchenpos.menu.domain.repository;

import kitchenpos.menu.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
