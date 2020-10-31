package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {

    private String name;

    public MenuGroupRequest() {//TODO : private
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
