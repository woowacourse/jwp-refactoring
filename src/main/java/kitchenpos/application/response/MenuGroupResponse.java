package kitchenpos.application.response;

import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {

    private Long id;
    private String name;

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        final MenuGroupResponse menuGroupResponse = new MenuGroupResponse();
        menuGroupResponse.id = menuGroup.getId();
        menuGroupResponse.name = menuGroup.getName();
        return menuGroupResponse;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

