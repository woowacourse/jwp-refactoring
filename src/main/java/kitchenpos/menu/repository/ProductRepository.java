package kitchenpos.menu.repository;

import kitchenpos.menu.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsById(Long productId);
}
