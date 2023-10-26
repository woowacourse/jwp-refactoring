package kitchenpos.dto.menugroup.request;

import kitchenpos.domain.menugroup.MenuGroup;

public class MenuGroupCreateRequest {
    private final String name;

    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }

    public String name() {
        return name;
    }
}
