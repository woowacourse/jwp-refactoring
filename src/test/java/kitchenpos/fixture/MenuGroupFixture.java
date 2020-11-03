package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    private static final String MENU_GROUP_NAME = "후라이드그룹";

    public static MenuGroup createMenuGroupWitId(Long id) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(MENU_GROUP_NAME);
        return menuGroup;
    }

    public static MenuGroup createMenuGroupWithoutId() {
        return createMenuGroupWitId(null);
    }

}
