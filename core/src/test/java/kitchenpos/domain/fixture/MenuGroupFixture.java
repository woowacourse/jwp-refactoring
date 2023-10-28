package kitchenpos.domain.fixture;

import kitchenpos.menu.MenuGroup;

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
