package kitchenpos.ui.dto.menugroup;

import kitchenpos.domain.MenuGroup;

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
