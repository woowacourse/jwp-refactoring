package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateRequest {

    private String name;

    public MenuGroupCreateRequest() {
    }

    public String getName() {
        return name;
    }

    public static MenuGroup toEntity(MenuGroupCreateRequest request) {
        return new MenuGroup(request.name);
    }
}
