package kitchenpos.ui.dto.menugroup;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {

    private final String name;

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
