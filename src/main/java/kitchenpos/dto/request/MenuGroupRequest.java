package kitchenpos.dto.request;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupRequest {

    private String name;

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return MenuGroup.ofNew(name);
    }

    public String getName() {
        return name;
    }

    private MenuGroupRequest() {
    }
}
