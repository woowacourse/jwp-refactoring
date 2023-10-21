package kitchenpos.menu.domain.repository;

import java.util.List;
import kitchenpos.menu.domain.Menu;

public interface MenuRepository {

  Menu save(final Menu entity);

  List<Menu> findAll();

  long countByIdIn(final List<Long> ids);
}
