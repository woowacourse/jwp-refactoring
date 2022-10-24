package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup createDefaultWithoutId() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("name");
        return menuGroup;
    }
}
