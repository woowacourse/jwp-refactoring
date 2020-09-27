package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static final MenuGroup MENU_GROUP_FIXTURE_고기 = new MenuGroup();
    public static final MenuGroup MENU_GROUP_FIXTURE_국 = new MenuGroup();

    static {
        MENU_GROUP_FIXTURE_고기.setName("고기");
        MENU_GROUP_FIXTURE_국.setName("국");
    }
}
