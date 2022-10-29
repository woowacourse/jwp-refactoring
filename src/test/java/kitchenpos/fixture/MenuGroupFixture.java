package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup newMenuGroup(final String name) {
        return new MenuGroup(
                name
        );
    }
}
