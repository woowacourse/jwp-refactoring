package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFactory {

    public static MenuGroup menuGroup(final String name) {
        final var menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return menuGroup;
    }
}
