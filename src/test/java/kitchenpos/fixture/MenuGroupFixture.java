package kitchenpos.fixture;

import java.util.Arrays;
import java.util.List;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {
    public static final MenuGroup MENU_GROUP1 = TestObjectUtils.createMenuGroup(1L, "두마리메뉴");

    public static final MenuGroup MENU_GROUP2 = TestObjectUtils.createMenuGroup(2L, "한마리메뉴");

    public static final List<MenuGroup> MENU_GROUPS = Arrays.asList(MENU_GROUP1, MENU_GROUP2);

}
