package kitchenpos.menugroup.dto.request;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupCreateRequest {
    private final Long id;
    private final String name;

    public MenuGroupCreateRequest(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(id, name);
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }
}
