package kitchenpos.dao;

import kitchenpos.domain.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuDao {

    Menu save(final Menu entity);

    Optional<Menu> findById(final Long id);

    List<Menu> findAll();
}
