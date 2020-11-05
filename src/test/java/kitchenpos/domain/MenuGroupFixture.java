package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.*;

public class MenuGroupFixture {

    public static MenuGroup createWithoutId() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("TEST_MENU_GROUP_NAME");

        return menuGroup;
    }

}