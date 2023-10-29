package kitchenpos.menu_group.persistence;

import kitchenpos.menu_group.persistence.entity.MenuGroupEntity;

import java.util.List;
import java.util.Optional;

public interface MenuGroupDao {

    MenuGroupEntity save(MenuGroupEntity entity);

    Optional<MenuGroupEntity> findById(Long id);

    List<MenuGroupEntity> findAll();

    boolean existsById(Long id);
}
