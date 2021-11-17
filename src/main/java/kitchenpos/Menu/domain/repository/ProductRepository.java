package kitchenpos.Menu.domain.repository;

import kitchenpos.Menu.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
