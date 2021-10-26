package kitchenpos.ui.factory;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFactory {

    public static MenuGroup create(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}
