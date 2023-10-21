package kitchenpos.application.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {

    private final String name;

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
