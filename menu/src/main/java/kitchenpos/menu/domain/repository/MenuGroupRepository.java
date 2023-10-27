package kitchenpos.menu.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.MenuGroup;

public interface MenuGroupRepository {

  MenuGroup save(MenuGroup menuGroup);

  Optional<MenuGroup> findById(Long id);

  List<MenuGroup> findAll();

  boolean existsById(Long id);
}
