package kitchenpos.menugroup.application.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupRequest {

    private final String name;

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
