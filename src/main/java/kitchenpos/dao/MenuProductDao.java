package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuProduct;

public interface MenuProductDao {

    Long save(MenuProduct entity);

    Optional<MenuProduct> findById(Long id);

    List<MenuProduct> findAll();

    List<MenuProduct> findAllByMenuId(Long menuId);
}
