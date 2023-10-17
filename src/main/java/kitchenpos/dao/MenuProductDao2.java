package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.entity.MenuProductEntity;
import kitchenpos.domain.MenuProduct;

public interface MenuProductDao2 {
    MenuProductEntity save(MenuProductEntity entity);

    Optional<MenuProductEntity> findById(Long id);

    List<MenuProductEntity> findAll();

    List<MenuProductEntity> findAllByMenuId(Long menuId);
}
