package kitchenpos.dto;

import kitchenpos.domain.menuGroup.MenuGroup;

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
