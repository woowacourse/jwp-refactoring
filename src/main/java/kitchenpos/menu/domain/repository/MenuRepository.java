package kitchenpos.menu.domain.repository;

import kitchenpos.menu.domain.Menu;

import java.util.List;

public interface MenuRepository {

    Menu save(final Menu entity);

    List<Menu> findAll();

    long countByIdIn(final List<Long> ids);
}
