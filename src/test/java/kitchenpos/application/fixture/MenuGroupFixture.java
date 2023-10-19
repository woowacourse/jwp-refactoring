package kitchenpos.application.fixture;

import kitchenpos.domain.menu.MenuGroup;

public abstract class MenuGroupFixture {

    private MenuGroupFixture() {
    }

    public static MenuGroup menuGroup(final String name) {
        return new MenuGroup(name);
    }

    public static MenuGroup western() {
        return new MenuGroup("양식");
    }
}
