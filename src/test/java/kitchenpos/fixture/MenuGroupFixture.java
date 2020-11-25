package kitchenpos.fixture;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupFixture {

    private static final String MENU_GROUP_NAME = "후라이드그룹";

    public static MenuGroup createMenuGroupWitId(Long id) {
        return new MenuGroup(id, MENU_GROUP_NAME);
    }

    public static MenuGroup createMenuGroupWithoutId() {
        return createMenuGroupWitId(null);
    }

}
