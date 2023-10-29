package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateRequest {

    private String name;

    protected MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
