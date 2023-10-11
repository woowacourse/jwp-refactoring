package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuFixture {

    public static MenuGroup 두마리메뉴() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("두마리메뉴");
        return menuGroup;
    }

    public static MenuGroup 한마리메뉴() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("한마리메뉴");
        return menuGroup;
    }

    public static MenuGroup 순살파닭두마리메뉴() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("순살파닭두마리메뉴");
        return menuGroup;
    }

    public static MenuGroup 신메뉴() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("신메뉴");
        return menuGroup;
    }
}
