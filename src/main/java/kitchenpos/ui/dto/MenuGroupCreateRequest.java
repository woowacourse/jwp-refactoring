package kitchenpos.ui.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateRequest {

    private String name;

    protected MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    public String getName() {
        return name;
    }
}
