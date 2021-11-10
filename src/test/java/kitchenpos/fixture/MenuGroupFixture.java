package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    private static final Long ID = 1L;
    private static final String NAME = "MENU_GROUP_NAME";

    public static MenuGroup createMenuGroup() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(ID);
        menuGroup.setName(NAME);
        return menuGroup;
    }
}
