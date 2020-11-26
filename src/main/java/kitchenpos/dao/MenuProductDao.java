package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;

import java.util.List;
import java.util.Optional;

public interface MenuProductDao {
    MenuProduct save(MenuProduct entity);

    void saveAll(List<MenuProduct> menuProducts);

    Optional<MenuProduct> findById(Long id);

    List<MenuProduct> findAll();

    List<MenuProduct> findAllByMenuId(Long menuId);
}
