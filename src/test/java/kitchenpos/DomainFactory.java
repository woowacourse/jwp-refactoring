package kitchenpos;

import kitchenpos.domain.MenuGroup;

public class DomainFactory {
    public static MenuGroup createMenuGroup(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
