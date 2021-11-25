package kitchenpos.menu.repository;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {

    List<MenuProduct> findAllByMenu(Menu menu);
}
