package kitchenpos.fixture;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup createChickenSetMenuGroupById(final Long id) {
        return new MenuGroup(id, "치킨세트");
    }

    public static final MenuGroup CHICKEN_SET = new MenuGroup(1L, "치킨세트");
    public static final MenuGroup CHICKEN_SET_NON_ID = new MenuGroup(null, "치킨세트");
}
