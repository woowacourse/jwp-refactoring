package kitchenpos.menu.repository;

import java.util.List;
import kitchenpos.menu.domain.MenuProduct;

public interface MenuProductRepository {
    MenuProduct save(MenuProduct entity);

    MenuProduct findById(Long id);

    List<MenuProduct> findAll();

    List<MenuProduct> findAllByMenuId(Long menuId);
}
