package kitchenpos.repository;

import java.util.Optional;
import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    Optional<MenuProduct> findByProductId(Long productId);
}
