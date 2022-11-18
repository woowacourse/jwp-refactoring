package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.MenuGroup;

public class MenuGroupSaveRequest {

    private String name;

    private MenuGroupSaveRequest() {
    }

    public MenuGroupSaveRequest(final String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
