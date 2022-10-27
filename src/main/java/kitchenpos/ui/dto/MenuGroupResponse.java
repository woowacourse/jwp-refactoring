package kitchenpos.ui.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {

    private long id;
    private String name;

    public MenuGroupResponse() {
    }

    public MenuGroupResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse of(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
