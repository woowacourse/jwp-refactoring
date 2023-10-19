package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.entity.MenuGroupEntity;

public interface MenuGroupDao {
    MenuGroupEntity save(MenuGroupEntity entity);

    Optional<MenuGroupEntity> findById(Long id);

    List<MenuGroupEntity> findAll();

    boolean existsById(Long id);
}
