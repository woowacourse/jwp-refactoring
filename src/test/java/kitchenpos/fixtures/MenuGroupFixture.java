package kitchenpos.fixtures;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup menuGroup1() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("첫번째 메뉴그룹");
        return menuGroup;
    }

    public static MenuGroup menuGroup2() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(2L);
        menuGroup.setName("두번째 메뉴그룹");
        return menuGroup;
    }

}
