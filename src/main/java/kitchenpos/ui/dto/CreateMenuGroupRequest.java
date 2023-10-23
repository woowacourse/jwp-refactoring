package kitchenpos.ui.dto;

import kitchenpos.domain.MenuGroup;

public class CreateMenuGroupRequest {

    private String name;

    public CreateMenuGroupRequest() {
    }

    public CreateMenuGroupRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }
}
