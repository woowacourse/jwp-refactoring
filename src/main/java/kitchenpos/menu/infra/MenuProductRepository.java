package kitchenpos.menu.infra;

import kitchenpos.menu.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
}
