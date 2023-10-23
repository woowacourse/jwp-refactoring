package kitchenpos.dto.menu;

import kitchenpos.domain.menu.MenuGroup;

public class CreateMenuGroupResponse {
    private final Long id;
    private final String name;

    private CreateMenuGroupResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static CreateMenuGroupResponse of(final Long id, final String name) {
        return new CreateMenuGroupResponse(id, name);
    }

    public static CreateMenuGroupResponse of(final MenuGroup menuGroup) {
        return new CreateMenuGroupResponse(menuGroup.getId(), menuGroup.getName().getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
