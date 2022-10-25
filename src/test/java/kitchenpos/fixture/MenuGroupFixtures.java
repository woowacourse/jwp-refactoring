package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixtures {

    public static MenuGroup createMenuGroup(final String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    public static MenuGroup 두마리메뉴() {
        return createMenuGroup("두마리메뉴");
    }

    public static MenuGroup 한마리메뉴() {
        return createMenuGroup("한마리메뉴");
    }

    public static MenuGroup 순살파닭두마리메뉴() {
        return createMenuGroup("순살파닭두마리메뉴");
    }

    public static MenuGroup 신메뉴() {
        return createMenuGroup("신메뉴");
    }
}
