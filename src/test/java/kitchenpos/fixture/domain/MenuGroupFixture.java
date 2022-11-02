package kitchenpos.fixture.domain;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup createMenuGroup(String name) {
        return createMenuGroup(null, name);
    }

    public static MenuGroup createMenuGroup(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}
