package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.entity.MenuEntity;
import kitchenpos.domain.Menu;

public interface MenuDao2 {
    MenuEntity save(MenuEntity entity);

    Optional<MenuEntity> findById(Long id);

    List<MenuEntity> findAll();

    long countByIdIn(List<Long> ids);
}
