package kitchenpos.fixtures.domain;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup createMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    public static MenuGroup createMenuGroupRequest() {
        return createMenuGroup("분식");
    }

    public static MenuGroup createMenuGroupRequest(final String name) {
        return createMenuGroup(name);
    }
}
