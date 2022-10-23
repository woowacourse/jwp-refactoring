package kitchenpos.application.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixtures {

    public static final MenuGroup generateMenuGroup(final String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    public static final MenuGroup 두마리메뉴() {
        return generateMenuGroup("두마리메뉴");
    }

    public static final MenuGroup 한마리메뉴() {
        return generateMenuGroup("한마리메뉴");
    }

    public static final MenuGroup 순살파닭두마리메뉴() {
        return generateMenuGroup("순살파닭두마리메뉴");
    }

    public static final MenuGroup 신메뉴() {
        return generateMenuGroup("신메뉴");
    }
}
