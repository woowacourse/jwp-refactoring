package kitchenpos.fixtures;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 치킨() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("치킨");
        return menuGroup;
    }

    public static MenuGroup 디저트() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(2L);
        menuGroup.setName("디저트");
        return menuGroup;
    }

    public static MenuGroup 양식() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(3L);
        menuGroup.setName("양식");
        return menuGroup;
    }

}
