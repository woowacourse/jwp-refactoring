package kitchenpos.dto;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupResponse {
    private Long id;

    public MenuGroupResponse() {
    }

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }
}
