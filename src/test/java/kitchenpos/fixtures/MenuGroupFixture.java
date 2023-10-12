package kitchenpos.fixtures;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup MENU_GROUP() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("kokodak");
        return menuGroup;
    }
}
