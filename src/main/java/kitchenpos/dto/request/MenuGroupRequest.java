package kitchenpos.dto.request;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {

    private final String name;

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
