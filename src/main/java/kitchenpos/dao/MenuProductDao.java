package kitchenpos.dao;

import kitchenpos.domain.menu.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuProductDao extends JpaRepository<MenuProduct, Long> {
    List<MenuProduct> findAllByMenuId(Long menu_id);
}
