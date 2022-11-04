package kitchenpos.menu;

import java.util.List;
import java.util.Optional;

public interface MenuRepository {

    Menu save(final Menu entity);

    Optional<Menu> findById(final Long id);

    List<Menu> findAll();
}
