package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {

    private long id;
    private String name;

    public MenuGroupResponse(final long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName().getValue());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
