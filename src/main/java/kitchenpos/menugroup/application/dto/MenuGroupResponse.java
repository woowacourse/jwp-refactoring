package kitchenpos.menugroup.application.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupResponse {
    private final Long id;
    private final String name;

    public MenuGroupResponse(MenuGroup menuGroup) {
        this.id = menuGroup.getId();
        this.name = menuGroup.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
