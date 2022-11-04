package kitchenpos.menugroup.domain;

import java.util.List;

public interface MenuGroupDao {
    MenuGroup save(MenuGroup entity);

    List<MenuGroup> findAll();

    boolean existsById(Long id);
}
