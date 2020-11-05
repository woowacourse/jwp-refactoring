package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static final String NAME = "DD";

    public static MenuGroup createWithoutId() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(NAME);

        return menuGroup;
    }

    public static MenuGroup createWithId(Long id) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(NAME);

        return menuGroup;
    }
}
