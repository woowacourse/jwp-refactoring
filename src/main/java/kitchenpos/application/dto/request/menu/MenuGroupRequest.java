package kitchenpos.application.dto.request.menu;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupRequest {

    private String name;

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public static MenuGroupRequest create(MenuGroup menuGroup) {
        return new MenuGroupRequest(menuGroup.getName());
    }

    public MenuGroup toEntity() {
        return new MenuGroup.MenuGroupBuilder()
                .setName(name)
                .build();
    }
}
