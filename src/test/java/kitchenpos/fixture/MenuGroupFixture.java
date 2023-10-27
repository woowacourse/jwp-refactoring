package kitchenpos.fixture;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup MENU_GROUP1 = new MenuGroup(1L, "두마리메뉴");
    public static MenuGroup MENU_GROUP2 = new MenuGroup(2L, "한마리메뉴");
    public static MenuGroup MENU_GROUP3 = new MenuGroup(3L, "순살파닭두마리메뉴");
    public static MenuGroup MENU_GROUP4 = new MenuGroup(4L, "신메뉴");

    private MenuGroupFixture() {
    }
}
