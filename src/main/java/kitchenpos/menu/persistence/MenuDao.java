package kitchenpos.menu.persistence;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.application.dto.MenuPersistence;

public interface MenuDao {

  MenuPersistence save(MenuPersistence entity);

  Optional<MenuPersistence> findById(Long id);

  List<MenuPersistence> findAll();

  long countByIdIn(List<Long> ids);
}
