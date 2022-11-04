package kitchenpos.menu.repository;

import java.util.List;
import kitchenpos.menu.domain.Menu;

public interface MenuRepository {
    Menu save(Menu entity);

    Menu findById(Long id);

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
