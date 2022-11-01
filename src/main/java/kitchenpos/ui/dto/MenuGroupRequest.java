package kitchenpos.ui.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {

    private String name;

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    private MenuGroupRequest() {
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }
}
