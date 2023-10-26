package kitchenpos.menu_group.domain.repository;

import kitchenpos.menu_group.domain.MenuGroup;

import java.util.List;

public interface MenuGroupRepository {

    MenuGroup save(final MenuGroup entity);

    boolean existsById(final Long id);

    List<MenuGroup> findAll();
}
