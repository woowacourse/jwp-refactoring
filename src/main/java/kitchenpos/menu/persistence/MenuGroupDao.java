package kitchenpos.menu.persistence;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.application.entity.MenuGroupEntity;

public interface MenuGroupDao {

  MenuGroupEntity save(MenuGroupEntity entity);

  Optional<MenuGroupEntity> findById(Long id);

  List<MenuGroupEntity> findAll();

  boolean existsById(Long id);
}
