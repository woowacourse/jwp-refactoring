package kitchenpos.application.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup createWithoutId() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("TEST_MENU_GROUP_NAME");

        return menuGroup;
    }

}