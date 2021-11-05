package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    private static final long ID = 1L;
    private static final String NAME = "이달의 메뉴";

    public static MenuGroup create() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(ID);
        menuGroup.setName(NAME);

        return menuGroup;
    }
}
