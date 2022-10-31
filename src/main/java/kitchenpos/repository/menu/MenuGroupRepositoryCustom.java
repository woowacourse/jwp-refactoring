package kitchenpos.repository.menu;

import kitchenpos.domain.menu.MenuGroup;

public interface MenuGroupRepositoryCustom {

    boolean existsBy(MenuGroup menuGroup);
}
