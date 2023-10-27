package kitchenpos.menu.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

public interface MenuDao {

  MenuEntity save(MenuEntity entity);

  Optional<MenuEntity> findById(Long id);

  List<MenuEntity> findAll();

  long countByIdIn(List<Long> ids);
}
