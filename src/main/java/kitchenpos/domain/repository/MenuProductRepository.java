package kitchenpos.domain.repository;

import kitchenpos.domain.MenuProduct;

import java.util.List;
import java.util.Optional;

public interface MenuProductRepository {

    MenuProduct save(MenuProduct entity);

    Optional<MenuProduct> findById(Long id);

    List<MenuProduct> findAll();

    List<MenuProduct> findAllByMenuId(Long menuId);

    List<MenuProduct> saveAll(List<MenuProduct> menuProducts);
}
