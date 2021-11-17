package kitchenpos.ui.dto;

import kitchenpos.domain.menugroup.MenuGroup;

public class MenuGroupResponse {
    private String name;

    public MenuGroupResponse(String name) {
        this.name = name;
    }

    public static MenuGroupResponse of(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getName());
    }

    public String getName() {
        return name;
    }
}
