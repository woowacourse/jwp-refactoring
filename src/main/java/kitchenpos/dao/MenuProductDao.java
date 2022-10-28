package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuProduct;

@Deprecated
public interface MenuProductDao {
    MenuProduct save(MenuProduct entity);

    Optional<MenuProduct> findById(Long id);

    List<MenuProduct> findAll();

    List<MenuProduct> findAllByMenuId(Long menuId);
}
