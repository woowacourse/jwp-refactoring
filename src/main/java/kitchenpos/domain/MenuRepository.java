package kitchenpos.domain;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.entity.MenuEntity;

public interface MenuRepository {

  Menu2 save(Menu2 menu);

  Optional<Menu2> findById(Long id);

  List<Menu2> findAll();

  long countByIdIn(List<Long> ids);
}
