package kitchenpos.menugroup.application.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupRequest {

    private String name;

    private MenuGroupRequest() {
    }

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return MenuGroup.from(name);
    }

    public String getName() {
        return name;
    }
}
