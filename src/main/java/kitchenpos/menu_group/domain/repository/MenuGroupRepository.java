package kitchenpos.menu_group.domain.repository;

import java.util.List;
import kitchenpos.menu_group.domain.MenuGroup;

public interface MenuGroupRepository {

  MenuGroup save(final MenuGroup entity);

  boolean existsById(final Long id);

  List<MenuGroup> findAll();
}
