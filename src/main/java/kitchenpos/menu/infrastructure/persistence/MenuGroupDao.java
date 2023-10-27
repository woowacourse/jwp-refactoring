package kitchenpos.menu.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

public interface MenuGroupDao {

  MenuGroupEntity save(MenuGroupEntity entity);

  Optional<MenuGroupEntity> findById(Long id);

  List<MenuGroupEntity> findAll();

  boolean existsById(Long id);
}
