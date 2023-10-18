package kitchenpos.dto.request;

import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateRequest {

    private final String name;

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }

    public String name() {
        return name;
    }
}
