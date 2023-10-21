package kitchenpos.menu.persistence;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.application.entity.MenuEntity;

public interface MenuDao {

  MenuEntity save(MenuEntity entity);

  Optional<MenuEntity> findById(Long id);

  List<MenuEntity> findAll();

  long countByIdIn(List<Long> ids);
}
