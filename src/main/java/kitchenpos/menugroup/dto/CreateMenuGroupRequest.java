package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class CreateMenuGroupRequest {

    private String name;

    public CreateMenuGroupRequest() {
    }

    public CreateMenuGroupRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }
}
