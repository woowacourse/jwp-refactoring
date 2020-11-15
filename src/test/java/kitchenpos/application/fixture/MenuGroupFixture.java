package kitchenpos.application.fixture;

import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup createWithoutId() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("TEST_MENU_GROUP_NAME");

        return menuGroup;
    }

}