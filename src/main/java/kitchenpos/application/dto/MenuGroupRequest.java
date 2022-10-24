package kitchenpos.application.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {
    private String name;

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }
}
