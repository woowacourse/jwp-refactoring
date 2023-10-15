package kitchenpos.support.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup getMenuGroup(final String name) {
        return new MenuGroup(name);
    }
}
