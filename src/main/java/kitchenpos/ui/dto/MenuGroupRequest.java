package kitchenpos.ui.dto;

import kitchenpos.domain.MenuGroup;

import javax.validation.constraints.NotNull;

public class MenuGroupRequest {
    @NotNull
    private String name;

    private MenuGroupRequest() {}

    public String getName() {
        return name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }
}
