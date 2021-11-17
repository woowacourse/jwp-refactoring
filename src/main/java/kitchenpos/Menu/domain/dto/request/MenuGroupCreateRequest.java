package kitchenpos.Menu.domain.dto.request;

import kitchenpos.Menu.domain.MenuGroup;

public class MenuGroupCreateRequest {

    private String name;

    protected MenuGroupCreateRequest() {
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
