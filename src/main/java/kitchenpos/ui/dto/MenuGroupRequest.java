package kitchenpos.ui.dto;

import kitchenpos.domain.menugroup.MenuGroup;

public class MenuGroupRequest {

    private String name;

    private MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return MenuGroup.entityOf(name);
    }

    @Override
    public String toString() {
        return "MenuGroupRequest{" +
            "name='" + name + '\'' +
            '}';
    }
}
