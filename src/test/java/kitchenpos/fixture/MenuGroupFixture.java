package kitchenpos.fixture;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupFixture {
    
    public static MenuGroup MENU_GROUP(String name) {
        MenuGroup menuGroup = new MenuGroup(name);
        return menuGroup;
    }
}
