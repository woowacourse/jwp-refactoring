package kitchenpos.dto;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupRequest {
    private String name;

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(final String name) {
        this.name = name;
    }


    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }
    public String getName() {
        return name;
    }
}
