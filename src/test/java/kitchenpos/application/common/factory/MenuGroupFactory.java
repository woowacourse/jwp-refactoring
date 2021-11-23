package kitchenpos.application.common.factory;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFactory {

    private MenuGroupFactory() {}

    public static MenuGroup create(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
