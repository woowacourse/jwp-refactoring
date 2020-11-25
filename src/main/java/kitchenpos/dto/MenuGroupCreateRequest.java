package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateRequest {

    private String name;

    public MenuGroupCreateRequest() {
    }

    private MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public static MenuGroupCreateRequest of(MenuGroup menuGroup) {
        return new MenuGroupCreateRequest(menuGroup.getName());
    }

    public String getName() {
        return name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(null, this.name);
    }
}
