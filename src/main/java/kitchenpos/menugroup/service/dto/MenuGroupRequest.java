package kitchenpos.menugroup.service.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupRequest {

    private String name;

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroupRequest() {
    }

    public String getName() {
        return name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }
}
