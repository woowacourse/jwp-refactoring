package kitchenpos.Menu.domain.repository;

import kitchenpos.Menu.domain.Menu;
import kitchenpos.Menu.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {

    List<MenuProduct> findAllByMenu(Menu menu);
}
