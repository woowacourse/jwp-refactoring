package kitchenpos.support.fixture.domain;

import kitchenpos.menugroup.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup getMenuGroup(final String name) {
        return new MenuGroup(name);
    }
}
