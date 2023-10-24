package kitchenpos.menugroup.dto.request;

import kitchenpos.menugroup.domain.MenuGroup;

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
