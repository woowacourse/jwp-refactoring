package kitchenpos.application.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 치킨() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("치킨");

        return menuGroup;
    }

    public static MenuGroup 피자() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("피자");

        return menuGroup;
    }
}
