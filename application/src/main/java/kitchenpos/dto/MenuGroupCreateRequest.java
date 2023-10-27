package kitchenpos.dto;

import kitchenpos.domain.menu_group.MenuGroup;

public class MenuGroupCreateRequest {

    private String name;

    public MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static MenuGroup toEntity(MenuGroupCreateRequest request) {
        return new MenuGroup(request.name);
    }
}
