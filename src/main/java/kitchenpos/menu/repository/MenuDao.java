package kitchenpos.menu.repository;

import kitchenpos.menu.domain.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuDao {

    Menu save(final Menu entity);

    Optional<Menu> findById(final Long id);

    List<Menu> findAll();
}
