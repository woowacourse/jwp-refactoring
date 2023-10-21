package kitchenpos.application.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup createMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return menuGroup;
    }

    public static MenuGroup createMenuGroup(final Long id, final String name) {
        final MenuGroup menuGroup = createMenuGroup(name);
        menuGroup.setId(id);

        return menuGroup;
    }
}
