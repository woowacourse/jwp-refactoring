package kitchenpos.application.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {

    private final long id;
    private final String name;

    public MenuGroupResponse(final long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
