package kitchenpos.menu.infrastructure;

import java.util.List;
import kitchenpos.menu.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuProductRepository extends JpaRepository<MenuProduct, Long> {
    List<MenuProduct> findAllByMenu_id(Long menuId);
}
