package kitchenpos.application.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup createMenuGroup(final String name) {
        return new MenuGroup(name);
    }

    public static MenuGroup createMenuGroup(final Long id, final String name) {
        return new MenuGroup(id, name);
    }
}
