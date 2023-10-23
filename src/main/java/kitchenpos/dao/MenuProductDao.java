package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.entity.MenuProductEntity;

public interface MenuProductDao {

  MenuProductEntity save(MenuProductEntity entity);

  Optional<MenuProductEntity> findById(Long id);

  List<MenuProductEntity> findAll();

  List<MenuProductEntity> findAllByMenuId(Long menuId);
}
