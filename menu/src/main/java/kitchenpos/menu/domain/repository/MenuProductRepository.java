package kitchenpos.menu.domain.repository;

import java.util.Optional;
import kitchenpos.menu.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    Optional<MenuProduct> findByProductId(Long productId);
}
