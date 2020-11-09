package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static final MenuGroup MENU_GROUP_FIXTURE_1 = new MenuGroup();
    public static final MenuGroup MENU_GROUP_FIXTURE_2 = new MenuGroup();

    static {
        MENU_GROUP_FIXTURE_1.setName("고기");
        MENU_GROUP_FIXTURE_2.setName("국");
    }
}
