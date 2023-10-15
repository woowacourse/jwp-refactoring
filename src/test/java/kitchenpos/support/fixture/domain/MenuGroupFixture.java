package kitchenpos.support.fixture.domain;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup getMenuGroup(final String name) {
        return new MenuGroup(name);
    }
}
