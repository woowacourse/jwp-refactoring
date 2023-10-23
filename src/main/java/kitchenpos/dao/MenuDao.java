package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.entity.MenuEntity;

public interface MenuDao {

  MenuEntity save(MenuEntity entity);

  Optional<MenuEntity> findById(Long id);

  List<MenuEntity> findAll();

  long countByIdIn(List<Long> ids);
}
