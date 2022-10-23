package kitchenpos.application.fixture;

import kitchenpos.domain.MenuGroup;

public enum MenuGroupFixture {

    MENUGROUP_세트메뉴A(createMenuGroup("세트메뉴A")),
    MENUGROUP_세트메뉴B(createMenuGroup("세트메뉴B")),
    MENUGROUP_세트메뉴C(createMenuGroup("세트메뉴C"));

    private final MenuGroup value;

    MenuGroupFixture(final MenuGroup value) {
        this.value = value;
    }

    public MenuGroup create() {
        return value;
    }

    public static MenuGroup createMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return menuGroup;
    }
}
