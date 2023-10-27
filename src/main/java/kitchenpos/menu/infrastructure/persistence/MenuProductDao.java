package kitchenpos.menu.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

public interface MenuProductDao {

  MenuProductEntity save(MenuProductEntity entity);

  Optional<MenuProductEntity> findById(Long id);

  List<MenuProductEntity> findAll();

  List<MenuProductEntity> findAllByMenuId(Long menuId);
}
