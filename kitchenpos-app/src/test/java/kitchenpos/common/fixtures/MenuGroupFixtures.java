package kitchenpos.common.fixtures;


import kitchenpos.domain.menugroup.MenuGroup;

public class MenuGroupFixtures {

    /**
     * NAME
     */
    public static final String MENU_GROUP1_NAME = "두마리메뉴";
    public static final String MENU_GROUP2_NAME = "한마리메뉴";

    /**
     * ENTITY
     */
    public static MenuGroup MENU_GROUP1() {
        return new MenuGroup(MENU_GROUP1_NAME);
    }

    public static MenuGroup MENU_GROUP2() {
        return new MenuGroup(MENU_GROUP2_NAME);
    }
}
