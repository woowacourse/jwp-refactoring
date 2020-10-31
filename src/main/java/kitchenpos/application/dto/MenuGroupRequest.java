package kitchenpos.application.dto;

import kitchenpos.domain.model.MenuGroup;

public class MenuGroupRequest {
    private String name;

    private MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(null, name);
    }

    public String getName() {
        return name;
    }
}
