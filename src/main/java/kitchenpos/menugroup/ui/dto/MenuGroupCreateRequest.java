package kitchenpos.menugroup.ui.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupCreateRequest {

    private String name;

    private MenuGroupCreateRequest() {
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
