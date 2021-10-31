package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    private static final MenuGroup menuGroup = new MenuGroup();

    public static MenuGroup menuGroup() {
        return menuGroup;
    }
}
