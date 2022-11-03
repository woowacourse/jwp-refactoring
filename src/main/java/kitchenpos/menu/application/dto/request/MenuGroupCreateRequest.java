package kitchenpos.menu.application.dto.request;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupCreateRequest {

    private final String name;

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
