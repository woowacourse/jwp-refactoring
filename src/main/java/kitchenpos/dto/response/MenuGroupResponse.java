package kitchenpos.dto.response;

import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {

    private final Long id;
    private final String name;

    private MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(MenuGroup savedMenuGroup) {
        return new MenuGroupResponse(savedMenuGroup.getId(), savedMenuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
