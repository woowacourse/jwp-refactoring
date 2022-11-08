package kitchenpos.menu.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Menus;

public interface MenuDao {

    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);

    Menus findAllByIdIn(List<Long> ids);
}
