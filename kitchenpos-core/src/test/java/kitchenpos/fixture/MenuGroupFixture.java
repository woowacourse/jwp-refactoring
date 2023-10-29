package kitchenpos.fixture;

import kitchenpos.domain.menugroup.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup menuGroup(String name) {
        return new MenuGroup(name);
    }
}
