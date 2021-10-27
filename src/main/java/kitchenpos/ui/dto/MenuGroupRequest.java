package kitchenpos.ui.dto;

import kitchenpos.domain.MenuGroup;

import javax.validation.constraints.NotNull;

public class MenuGroupRequest {
    @NotNull
    private String name;

    private MenuGroupRequest() {}

    private MenuGroupRequest(String name) {
        this.name = name;
    }

    public static MenuGroupRequest from(String name) {
        return new MenuGroupRequest(name);
    }

    public String getName() {
        return name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }
}
