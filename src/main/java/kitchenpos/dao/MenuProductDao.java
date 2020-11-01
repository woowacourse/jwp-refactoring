package kitchenpos.dao;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuProductDao extends JpaRepository<MenuProduct, Long> {
    List<MenuProduct> findAllByMenu(Menu menu);
}
