package kitchenpos.menu.application.dto;

import kitchenpos.menu.domain.MenuGroup;

public final class MenuGroupResponse {

    private final Long id;
    private final String name;

    public static MenuGroupResponse from(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    private MenuGroupResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
