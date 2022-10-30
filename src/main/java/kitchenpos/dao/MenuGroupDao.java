package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.MenuGroup;

public interface MenuGroupDao {
    MenuGroup save(MenuGroup entity);

    MenuGroup findById(Long id);

    List<MenuGroup> findAll();

    boolean existsById(Long id);
}
