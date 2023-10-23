package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {

    private final Long id;
    private final String name;

    public MenuGroupResponse(
            final Long id,
            final String name
    ) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse toResponse(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
