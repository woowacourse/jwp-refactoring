package kitchenpos.menu.domain;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;

public interface MenuDao {

    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
