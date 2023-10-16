package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public final class MenuGroupFixture {

    public static MenuGroup 일식() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("일식");
        return menuGroup;
    }

    public static MenuGroup 한식() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("한식");
        return menuGroup;
    }
}
