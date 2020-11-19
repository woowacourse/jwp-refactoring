package kitchenpos.application.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateRequest {
    private String name;

    private MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(null, name);
    }
}
