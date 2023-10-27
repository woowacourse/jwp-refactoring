package kitchenpos.menu.persistence;

import kitchenpos.menu.persistence.entity.MenuProductEntity;

import java.util.List;
import java.util.Optional;

public interface MenuProductDao {

    MenuProductEntity save(MenuProductEntity entity);

    Optional<MenuProductEntity> findById(Long id);

    List<MenuProductEntity> findAll();

    List<MenuProductEntity> findAllByMenuId(Long menuId);
}
