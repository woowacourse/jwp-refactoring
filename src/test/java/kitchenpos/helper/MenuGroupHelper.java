package kitchenpos.helper;

import kitchenpos.domain.MenuGroup;

public class MenuGroupHelper {
    private MenuGroupHelper() {
    }

    public static MenuGroup createMenuGroup(String menuGroupName) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(menuGroupName);
        return menuGroup;
    }
}
