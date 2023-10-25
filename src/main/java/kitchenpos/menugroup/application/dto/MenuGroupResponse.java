package kitchenpos.menugroup.application.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupResponse {

    private long id;
    private String name;

    private MenuGroupResponse(final long id, final String name) {
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
