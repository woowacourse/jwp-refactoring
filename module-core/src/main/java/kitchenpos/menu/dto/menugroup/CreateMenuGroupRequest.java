package kitchenpos.menu.dto.menugroup;

import kitchenpos.menu.domain.MenuGroup;

public class CreateMenuGroupRequest {

    private final String name;

    public CreateMenuGroupRequest(final String name) {
        this.name = name;
    }
    public MenuGroup toDomain() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
