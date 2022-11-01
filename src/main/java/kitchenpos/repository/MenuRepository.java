package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.menu.Menu;

public interface MenuRepository {
    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();
}
