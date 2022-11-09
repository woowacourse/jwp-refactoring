package kitchenpos.menugruop.application.dto;

import kitchenpos.menugruop.domain.MenuGroup;

public class MenuGroupCreateRequest {

    private String name;

    private MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }
}
