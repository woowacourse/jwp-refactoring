package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup newMenuGroup(final String name) {
        final var menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
