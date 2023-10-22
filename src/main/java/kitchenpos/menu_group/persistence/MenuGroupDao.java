package kitchenpos.menu_group.persistence;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu_group.application.entity.MenuGroupEntity;

public interface MenuGroupDao {

  MenuGroupEntity save(MenuGroupEntity entity);

  Optional<MenuGroupEntity> findById(Long id);

  List<MenuGroupEntity> findAll();

  boolean existsById(Long id);
}
