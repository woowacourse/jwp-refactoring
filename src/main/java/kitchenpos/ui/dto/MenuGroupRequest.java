package kitchenpos.ui.dto;

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

    public MenuGroup toEntity() {
        return new MenuGroup(this.name);
    }
}
