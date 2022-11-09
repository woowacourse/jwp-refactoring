package kitchenpos.menu.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.MenuProduct;

public interface MenuProductRepository {

    MenuProduct save(MenuProduct entity);

    Optional<MenuProduct> findById(Long id);

    List<MenuProduct> findAll();

    List<MenuProduct> findAllByMenuId(Long menuId);
}
