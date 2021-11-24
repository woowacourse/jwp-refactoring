package kitchenpos.menu.ui.request;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupCreateRequest {

    private String name;

    public static MenuGroupCreateRequest create(String name) {
        final MenuGroupCreateRequest menuGroup = new MenuGroupCreateRequest();
        menuGroup.name = name;
        return menuGroup;
    }

    public String getName() {
        return name;
    }

    public MenuGroup toEntity() {
        return MenuGroup.create(name);
    }
}
