package kitchenpos.domain.menu;

import java.util.List;

public interface MenuGroupRepository {

    MenuGroup get(Long id);

    MenuGroup add(MenuGroup menuGroup);

    List<MenuGroup> getAll();
}
