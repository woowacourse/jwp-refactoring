package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuFixture {

    public static MenuGroup createMenuGroup(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
