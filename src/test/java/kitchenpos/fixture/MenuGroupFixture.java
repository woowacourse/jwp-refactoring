package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup from(String name) {
        return new MenuGroup(name);
    }
}
