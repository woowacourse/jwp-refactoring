package kitchenpos.menu.repository;

import java.util.List;
import kitchenpos.menu.domain.MenuProduct;

public interface MenuProductRepository {
    MenuProduct save(MenuProduct entity);

    List<MenuProduct> saveAll(List<MenuProduct> menuProductValues);

    MenuProduct findById(Long id);

    List<MenuProduct> findAll();

    List<MenuProduct> findAllByMenuId(Long menuId);
}
