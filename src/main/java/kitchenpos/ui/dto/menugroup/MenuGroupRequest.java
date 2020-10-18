package kitchenpos.ui.dto.menugroup;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {

    private String name;

    protected MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }
}
