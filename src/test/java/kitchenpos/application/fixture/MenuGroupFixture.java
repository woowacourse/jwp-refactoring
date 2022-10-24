package kitchenpos.application.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 한마리_메뉴_그룹() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("한마리메뉴");

        return menuGroup;
    }

    public static MenuGroup 두마리_메뉴_그룹() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("두마리메뉴");

        return menuGroup;
    }
}
