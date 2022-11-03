package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.menu.MenuProduct;

public interface MenuProductDao {
    MenuProduct save(MenuProduct entity);

    MenuProduct findById(Long id);

    List<MenuProduct> findAll();

    List<MenuProduct> findAllByMenuId(Long menuId);
}
