package kitchenpos.dto.menu;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupRequest {
    private String name;

    private MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup to() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
