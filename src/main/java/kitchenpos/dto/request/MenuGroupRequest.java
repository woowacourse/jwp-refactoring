package kitchenpos.dto.request;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {

    private String name;

    private MenuGroupRequest() {
    }

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(this.name);

        return menuGroup;
    }

    public String getName() {
        return name;
    }
}
