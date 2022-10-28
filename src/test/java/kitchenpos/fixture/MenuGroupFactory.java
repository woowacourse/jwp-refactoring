package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFactory {

    public static MenuGroup menuGroup(final String name) {
        return new MenuGroup(null, name);
    }
}
