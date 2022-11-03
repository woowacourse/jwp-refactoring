package kitchenpos.fixture;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupFactory {

    public static MenuGroup menuGroup(final String name) {
        return new MenuGroup(name);
    }
}
