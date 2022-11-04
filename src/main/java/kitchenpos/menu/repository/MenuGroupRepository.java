package kitchenpos.menu.repository;

import java.util.List;
import kitchenpos.menu.domain.MenuGroup;

public interface MenuGroupRepository {
    MenuGroup save(MenuGroup entity);

    MenuGroup findById(Long id);

    List<MenuGroup> findAll();

    boolean existsById(Long id);
}
